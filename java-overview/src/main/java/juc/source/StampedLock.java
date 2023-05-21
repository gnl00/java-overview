package juc.source;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class StampedLock implements java.io.Serializable {
    /*
     * 通常来说，锁的主要状态主要由二进制数字串来表示，初始值为 256，二进制第 7 位为 0 表示此时锁未被占有
     * 有线程获第一次获取到锁 stamp = state + WBIT = 256 + 128 = 384，第一次获取锁时返回的 stamp = 384，二进制第 7 位为 1，表示锁此时被占有
     * Conceptually, the primary state of the lock includes a sequence
     * number that is odd when write-locked and even otherwise.
     * 这个序列号会被读锁的非零计数抵消
     * However, this is offset by a reader count that is non-zero when
     * read-locked.
     * 如果获取的是乐观锁，读计数会被忽略。因为需要用一个足够小的有限数来表示读计数
     * The read count is ignored when validating "optimistic" seqlock-reader-style stamps.
     * Because we must use a small finite number of bits (currently 7) for readers,
     * 读锁超过计数最大值时，使用 RBITS 来表示读锁溢出，作为更新溢出的自旋保护机制
     * a supplementary reader overflow word is used when the number of
     * readers exceeds the count field. We do this by treating the max
     * reader count value (RBITS) as a spinlock protecting overflow
     * updates.
     *
     * 等待队列使用的是 CLH 队列的修改版
     * Waiters use a modified form of CLH lock used in
     * AbstractQueuedSynchronizer (see its internal documentation for a fuller account),
     * 队列中的每个结点都有一个标记来表示当前结点是读结点还是写结点
     * where each node is tagged (field mode) as either a reader or writer.
     * 多个读结点将会组成一条读等待队列并连接到字段 cowait 下等待获取读锁
     * Sets of waiting readers are grouped (linked) under a common node (field cowait) so act as a single
     * node with respect to most CLH mechanics.
     * 得益于 CLH 队列的设计，在 cowait 链表上等待获取读锁的读结点并不需要携带 stamp 序列号
     * 因为我们从设计上可以知道队列中的后续结点肯定大于前面的读结点，并且运算逻辑也都清晰（）
     * By virtue of the queue structure, wait nodes need not actually carry sequence numbers;
     * we know each is greater than its predecessor.  This
     * simplifies the scheduling policy to a mainly-FIFO scheme that
     * incorporates elements of Phase-Fair locks (see Brandenburg &
     * Anderson, especially http://www.cs.unc.edu/~bbb/diss/).  In
     * particular, we use the phase-fair anti-barging rule: If an
     * incoming reader arrives while read lock is held but there is a
     * queued writer, this incoming reader is queued.  (This rule is
     * responsible for some of the complexity of method acquireRead,
     * but without it, the lock becomes highly unfair.) Method release
     * does not (and sometimes cannot) itself wake up cowaiters. This
     * is done by the primary thread, but helped by any other threads
     * with nothing better to do in methods acquireRead and
     * acquireWrite.
     *
     * These rules apply to threads actually queued. All tryLock forms
     * opportunistically try to acquire locks regardless of preference
     * rules, and so may "barge" their way in.  Randomized spinning is
     * used in the acquire methods to reduce (increasingly expensive)
     * context switching while also avoiding sustained memory
     * thrashing among many threads.  We limit spins to the head of
     * queue. A thread spin-waits up to SPINS times (where each
     * iteration decreases spin count with 50% probability) before
     * blocking. If, upon wakening it fails to obtain lock, and is
     * still (or becomes) the first waiting thread (which indicates
     * that some other thread barged and obtained lock), it escalates
     * spins (up to MAX_HEAD_SPINS) to reduce the likelihood of
     * continually losing to barging threads.
     *
     * 几乎所有的机制都会在 acquireWrite 和 acquireRead 这两个方法中执行
     * Nearly all of these mechanics are carried out in methods
     * acquireWrite and acquireRead, that, as typical of such code,
     * sprawl out because actions and retries rely on consistent sets
     * of locally cached reads.
     *
     * 对于普通的 state 验证方法来说，validate() 方法中对锁序列的验证更加严格的顺序要求
     * As noted in Boehm's paper (above), sequence validation (mainly
     * method validate()) requires stricter ordering rules than apply
     * to normal volatile reads (of "state").  To force orderings of
     * reads before a validation and the validation itself in those
     * cases where this is not already forced, we use
     * Unsafe.loadFence. 内存屏障，禁止load操作重排序。屏障前的load操作不能被重排序到屏障后，屏障后的load操作不能被重排序到屏障前
     *
     * The memory layout keeps lock state and queue pointers together
     * (normally on the same cache line). This usually works well for
     * read-mostly loads. In most other cases, the natural tendency of
     * adaptive-spin CLH locks to reduce memory contention lessens
     * motivation to further spread out contended locations, but might
     * be subject to future improvements.
     */

    private static final long serialVersionUID = -6001602636862214147L;

    /** Number of processors, for spin control */
    private static final int NCPU = Runtime.getRuntime().availableProcessors();

    /** Maximum number of retries before enqueuing on acquisition */
    // Assume that your CPU have 4 cores, SPINS should be 64, if single core should be 1
    // 线程结点入队最大自旋次数
    private static final int SPINS = (NCPU > 1) ? 1 << 6 : 0;

    /** Maximum number of retries before blocking at head on acquisition */
    // 队列头结点获取锁失败后自旋次数 NCPU ? 2^10=1024 : 0
    private static final int HEAD_SPINS = (NCPU > 1) ? 1 << 10 : 0;

    /** Maximum number of retries before re-blocking */
    // 队列头结点获取锁失败后最大自旋次数 NCPU ? 2^16=65536 : 0
    private static final int MAX_HEAD_SPINS = (NCPU > 1) ? 1 << 16 : 0;

    // 等待溢出自旋锁时让出的时间
    /** The period for yielding when waiting for overflow spinlock */
    private static final int OVERFLOW_YIELD_RATE = 7; // must be power 2 - 1

    // 溢出前用于读计数的位数
    /** The number of bits to use for reader count before overflowing */
    private static final int LG_READERS = 7;

    // Values for lock state and stamp operations
    private static final long RUNIT = 1L; // 每次获取读锁的计数单位，进行+1
    private static final long WBIT  = 1L << LG_READERS; // 128，二进制 1000 0000 每次获取写锁的计数单位
    private static final long RBITS = WBIT - 1L; // 127，0111 1111，用于读锁溢出保护
    private static final long RFULL = RBITS - 1L; // 126，0111 1110，最大读线程数
    private static final long ABITS = RBITS | WBIT; // 255，二进制 1111 1111，用于辅助判断是否有写锁
    private static final long SBITS = ~RBITS; // note overlap with ABITS 取反 1000 0000

    // Initial value for lock state; avoid failure value zero
    private static final long ORIGIN = WBIT << 1; // 256, state 的初始值

    // 适用于取消锁获取方法的特殊值
    // Special value from cancelled acquire methods so caller can throw IE
    private static final long INTERRUPTED = 1L;

    // WNode 结点的 status 值
    // Values for node status; order matters
    private static final int WAITING   = -1;
    private static final int CANCELLED =  1;

    // WNode 结点的读写模式
    // Modes for nodes (int not boolean to allow arithmetic)
    private static final int RMODE = 0;
    private static final int WMODE = 1;

    /** Wait nodes */
    static final class WNode {
        volatile WNode prev;
        volatile WNode next;

        // 有多个读线程等待获取读锁时组成的链表
        volatile WNode cowait;    // list of linked readers
        volatile Thread thread;   // non-null while possibly parked
        volatile int status;      // 0, WAITING=-1, or CANCELLED=1
        final int mode;           // RMODE or WMODE
        WNode(int m, WNode p) { mode = m; prev = p; }
    }

    /** Head of CLH queue */
    private transient volatile WNode whead;
    /** Tail (last) of CLH queue */
    private transient volatile WNode wtail;

    // views
    transient ReadLockView readLockView;
    transient WriteLockView writeLockView;
    transient ReadWriteLockView readWriteLockView;

    /** Lock sequence/state */
    private transient volatile long state;
    /** extra reader count when state read count saturated */
    private transient int readerOverflow;

    /**
     * Creates a new lock, initially in unlocked state.
     */
    public StampedLock() {
        state = ORIGIN;
    }

    /**
     * 获取写锁，获取失败则阻塞，直到获取成功
     * Exclusively acquires the lock, blocking if necessary
     * until available.
     *
     * @return a stamp that can be used to unlock or convert mode
     */
    public long writeLock() {
        long s, next;  // bypass acquireWrite in fully unlocked case only
        // state=256, WBIT=128, ABITS=255, STATE=16 表示内存偏移量
        // state & ABITS == 0L 表示锁没有被获取
        // 如果锁没有被获取，并且 CAS 设置成功 返回 next = s + WBIT
        // 程序运行后第一次获取锁时返回的 stamp=256+128=384
        // 锁获取失败，进入 acquireWrite，在循环中自旋
        return ((((s = state) & ABITS) == 0L &&
                U.compareAndSwapLong(this, STATE, s, next = s + WBIT)) ?
                next : acquireWrite(false, 0L));
    }

    /**
     * Exclusively acquires the lock if it is immediately available.
     *
     * @return a stamp that can be used to unlock or convert mode,
     * or zero if the lock is not available
     */
    public long tryWriteLock() { // 非阻塞获取 writeLock
        long s, next;
        return ((((s = state) & ABITS) == 0L && // state & ABITS == 0L 表示当前无锁
                U.compareAndSwapLong(this, STATE, s, next = s + WBIT)) ?
                next : 0L);
    }

    /**
     * Exclusively acquires the lock if it is available within the
     * given time and the current thread has not been interrupted.
     * Behavior under timeout and interruption matches that specified
     * for method {@link Lock#tryLock(long,TimeUnit)}.
     *
     * @param time the maximum time to wait for the lock
     * @param unit the time unit of the {@code time} argument
     * @return a stamp that can be used to unlock or convert mode,
     * or zero if the lock is not available
     * @throws InterruptedException if the current thread is interrupted
     * before acquiring the lock
     */
    public long tryWriteLock(long time, TimeUnit unit)
            throws InterruptedException {
        long nanos = unit.toNanos(time);
        if (!Thread.interrupted()) {
            long next, deadline;
            if ((next = tryWriteLock()) != 0L)
                return next;
            if (nanos <= 0L)
                return 0L;
            if ((deadline = System.nanoTime() + nanos) == 0L)
                deadline = 1L;
            if ((next = acquireWrite(true, deadline)) != INTERRUPTED)
                return next;
        }
        throw new InterruptedException();
    }

    /**
     * Exclusively acquires the lock, blocking if necessary
     * until available or the current thread is interrupted.
     * Behavior under interruption matches that specified
     * for method {@link Lock#lockInterruptibly()}.
     *
     * @return a stamp that can be used to unlock or convert mode
     * @throws InterruptedException if the current thread is interrupted
     * before acquiring the lock
     */
    public long writeLockInterruptibly() throws InterruptedException {
        long next;
        if (!Thread.interrupted() &&
                (next = acquireWrite(true, 0L)) != INTERRUPTED)
            return next;
        throw new InterruptedException();
    }

    /**
     * Non-exclusively acquires the lock, blocking if necessary
     * until available.
     *
     * @return a stamp that can be used to unlock or convert mode
     */
    public long readLock() {
        // 假设前面已经有一个写线程
        long s = state, next;

        // bypass acquireRead on common uncontended case
        // 下列条件满足的话可以直接跳过 acquireRead 和锁竞争
        // ABITS=255，RFULL=126 表示最大读线程数
        // 存在写锁 s=state=384，state & ABITS = 128 > RFULL=126
        // 写锁已经释放 s=state=512，state & ABITS = 0
        return ((whead == wtail && (s & ABITS) < RFULL &&
                U.compareAndSwapLong(this, STATE, s, next = s + RUNIT)) ? // RUNIT=1
                next : acquireRead(false, 0L)); // 读锁获取失败，进入 acquireRead 自旋
    }

    /**
     * Non-exclusively acquires the lock if it is immediately available.
     *
     * @return a stamp that can be used to unlock or convert mode,
     * or zero if the lock is not available
     */
    public long tryReadLock() {
        for (;;) {
            long s, m, next;
            if ((m = (s = state) & ABITS) == WBIT)
                return 0L;
            else if (m < RFULL) {
                if (U.compareAndSwapLong(this, STATE, s, next = s + RUNIT))
                    return next;
            }
            else if ((next = tryIncReaderOverflow(s)) != 0L)
                return next;
        }
    }

    /**
     * Non-exclusively acquires the lock if it is available within the
     * given time and the current thread has not been interrupted.
     * Behavior under timeout and interruption matches that specified
     * for method {@link Lock#tryLock(long,TimeUnit)}.
     *
     * @param time the maximum time to wait for the lock
     * @param unit the time unit of the {@code time} argument
     * @return a stamp that can be used to unlock or convert mode,
     * or zero if the lock is not available
     * @throws InterruptedException if the current thread is interrupted
     * before acquiring the lock
     */
    public long tryReadLock(long time, TimeUnit unit)
            throws InterruptedException {
        long s, m, next, deadline;
        long nanos = unit.toNanos(time);
        if (!Thread.interrupted()) {
            if ((m = (s = state) & ABITS) != WBIT) {
                if (m < RFULL) {
                    if (U.compareAndSwapLong(this, STATE, s, next = s + RUNIT))
                        return next;
                }
                else if ((next = tryIncReaderOverflow(s)) != 0L)
                    return next;
            }
            if (nanos <= 0L)
                return 0L;
            if ((deadline = System.nanoTime() + nanos) == 0L)
                deadline = 1L;
            if ((next = acquireRead(true, deadline)) != INTERRUPTED)
                return next;
        }
        throw new InterruptedException();
    }

    /**
     * Non-exclusively acquires the lock, blocking if necessary
     * until available or the current thread is interrupted.
     * Behavior under interruption matches that specified
     * for method {@link Lock#lockInterruptibly()}.
     *
     * @return a stamp that can be used to unlock or convert mode
     * @throws InterruptedException if the current thread is interrupted
     * before acquiring the lock
     */
    public long readLockInterruptibly() throws InterruptedException {
        long next;
        if (!Thread.interrupted() &&
                (next = acquireRead(true, 0L)) != INTERRUPTED)
            return next;
        throw new InterruptedException();
    }

    /**
     * 乐观锁返回一个 stamp 来作为校验，不会真正的上锁，因此不用解锁
     * Returns a stamp that can later be validated, or zero
     * if exclusively locked.
     *
     * @return a stamp, or zero if exclusively locked
     */
    public long tryOptimisticRead() {
        long s;
        return (((s = state) & WBIT) == 0L) ? (s & SBITS) : 0L;
    }

    /**
     * Returns true if the lock has not been exclusively acquired
     * since issuance of the given stamp. Always returns false if the
     * stamp is zero. Always returns true if the stamp represents a
     * currently held lock. Invoking this method with a value not
     * obtained from {@link #tryOptimisticRead} or a locking method
     * for this lock has no defined effect or result.
     *
     * @param stamp a stamp
     * @return {@code true} if the lock has not been exclusively acquired
     * since issuance of the given stamp; else false
     */
    public boolean validate(long stamp) {
        U.loadFence(); // 内存屏障，禁止load操作重排序。屏障前的load操作不能被重排序到屏障后，屏障后的load操作不能被重排序到屏障前
        return (stamp & SBITS) == (state & SBITS);
    }

    /**
     * If the lock state matches the given stamp, releases the
     * exclusive lock.
     *
     * @param stamp a stamp returned by a write-lock operation
     * @throws IllegalMonitorStateException if the stamp does
     * not match the current state of this lock
     */
    public void unlockWrite(long stamp) { // 释放写锁，WBIT=128，会将 state 增加 WBIT
        // 假设是第一个获取到锁的线程在解锁 stamp=384
        WNode h;
        // state 不匹配或者不存在写锁，抛出异常
        if (state != stamp || (stamp & WBIT) == 0L)
            throw new IllegalMonitorStateException();
        // 进制位从 0 开始，第 7 位是写锁状态标志位，0 代表持有写锁，1 代表未持有写锁

        // state 初始值 256 ==> 0001 0000 0000 第 7 位 0  ==> Non-Lock
        // state firstLock 384 ==> 1 1000 0000 第 7 位 1 ==> Lock
        // state firstUnlock 512 ==> 10 0000 0000 第 7 位 0 ==> Non-Lock

        // 保证stamp的低8位为0
        state = (stamp += WBIT) == 0L ? ORIGIN : stamp; // 假设是第一个获取到锁的线程在解锁，解锁后 stamp=512
        if ((h = whead) != null && h.status != 0)
            release(h);
    }

    /**
     * If the lock state matches the given stamp, releases the
     * non-exclusive lock.
     *
     * @param stamp a stamp returned by a read-lock operation
     * @throws IllegalMonitorStateException if the stamp does
     * not match the current state of this lock
     */
    public void unlockRead(long stamp) { // 释放读锁
        long s, m; WNode h;
        for (;;) {
            // 假设已经有一个线程获取过写锁，此时 stamp=512
            // ABITS=255
            // 1. state != stamp state和传入的参数 stamp 不匹配
            // 2. stamp & ABITS == 0L || s & ABITS == 0L 都表示锁未被获取
            // 3. m = s & ABITS = 513 & 255 = 1 表示存在读线程
            // m == WBIT 表示当前是写模式
            if (((s = state) & SBITS) != (stamp & SBITS) ||
                    (stamp & ABITS) == 0L || (m = s & ABITS) == 0L || m == WBIT)
                throw new IllegalMonitorStateException();
            if (m < RFULL) { // 读线程数未溢出
                if (U.compareAndSwapLong(this, STATE, s, s - RUNIT)) { // RUNIT=1
                    // m == RUNIT 存在读线程
                    // 读线程等待队列头结点不为 null
                    // h.status != 0 头结点的状态不是0 可能是 WAITING=-1 OR  CANCELLED=1
                    if (m == RUNIT && (h = whead) != null && h.status != 0)
                        release(h); // 释放锁
                    break;
                }
            }
            else if (tryDecReaderOverflow(s) != 0L) // 尝试减少溢出的读线程数
                break;
        }
    }

    /**
     * If the lock state matches the given stamp, releases the
     * corresponding mode of the lock.
     *
     * @param stamp a stamp returned by a lock operation
     * @throws IllegalMonitorStateException if the stamp does
     * not match the current state of this lock
     */
    public void unlock(long stamp) {
        long a = stamp & ABITS, m, s; WNode h;
        while (((s = state) & SBITS) == (stamp & SBITS)) {
            if ((m = s & ABITS) == 0L)
                break;
            else if (m == WBIT) {
                if (a != m)
                    break;
                state = (s += WBIT) == 0L ? ORIGIN : s;
                if ((h = whead) != null && h.status != 0)
                    release(h);
                return;
            }
            else if (a == 0L || a >= WBIT)
                break;
            else if (m < RFULL) {
                if (U.compareAndSwapLong(this, STATE, s, s - RUNIT)) {
                    if (m == RUNIT && (h = whead) != null && h.status != 0)
                        release(h);
                    return;
                }
            }
            else if (tryDecReaderOverflow(s) != 0L)
                return;
        }
        throw new IllegalMonitorStateException();
    }

    /**
     * If the lock state matches the given stamp, performs one of
     * the following actions. If the stamp represents holding a write
     * lock, returns it.  Or, if a read lock, if the write lock is
     * available, releases the read lock and returns a write stamp.
     * Or, if an optimistic read, returns a write stamp only if
     * immediately available. This method returns zero in all other
     * cases.
     *
     * @param stamp a stamp
     * @return a valid write stamp, or zero on failure
     */
    public long tryConvertToWriteLock(long stamp) {
        long a = stamp & ABITS, m, s, next;
        while (((s = state) & SBITS) == (stamp & SBITS)) {
            if ((m = s & ABITS) == 0L) {
                if (a != 0L)
                    break;
                if (U.compareAndSwapLong(this, STATE, s, next = s + WBIT))
                    return next;
            }
            else if (m == WBIT) {
                if (a != m)
                    break;
                return stamp;
            }
            else if (m == RUNIT && a != 0L) {
                if (U.compareAndSwapLong(this, STATE, s,
                        next = s - RUNIT + WBIT))
                    return next;
            }
            else
                break;
        }
        return 0L;
    }

    /**
     * If the lock state matches the given stamp, performs one of
     * the following actions. If the stamp represents holding a write
     * lock, releases it and obtains a read lock.  Or, if a read lock,
     * returns it. Or, if an optimistic read, acquires a read lock and
     * returns a read stamp only if immediately available. This method
     * returns zero in all other cases.
     *
     * @param stamp a stamp
     * @return a valid read stamp, or zero on failure
     */
    public long tryConvertToReadLock(long stamp) {
        long a = stamp & ABITS, m, s, next; WNode h;
        while (((s = state) & SBITS) == (stamp & SBITS)) {
            if ((m = s & ABITS) == 0L) {
                if (a != 0L)
                    break;
                else if (m < RFULL) {
                    if (U.compareAndSwapLong(this, STATE, s, next = s + RUNIT))
                        return next;
                }
                else if ((next = tryIncReaderOverflow(s)) != 0L)
                    return next;
            }
            else if (m == WBIT) {
                if (a != m)
                    break;
                state = next = s + (WBIT + RUNIT);
                if ((h = whead) != null && h.status != 0)
                    release(h);
                return next;
            }
            else if (a != 0L && a < WBIT)
                return stamp;
            else
                break;
        }
        return 0L;
    }

    /**
     * If the lock state matches the given stamp then, if the stamp
     * represents holding a lock, releases it and returns an
     * observation stamp.  Or, if an optimistic read, returns it if
     * validated. This method returns zero in all other cases, and so
     * may be useful as a form of "tryUnlock".
     *
     * @param stamp a stamp
     * @return a valid optimistic read stamp, or zero on failure
     */
    public long tryConvertToOptimisticRead(long stamp) {
        long a = stamp & ABITS, m, s, next; WNode h;
        U.loadFence(); // 内存屏障，禁止load操作重排序。屏障前的load操作不能被重排序到屏障后，屏障后的load操作不能被重排序到屏障前
        for (;;) {
            if (((s = state) & SBITS) != (stamp & SBITS))
                break;
            if ((m = s & ABITS) == 0L) {
                if (a != 0L)
                    break;
                return s;
            }
            else if (m == WBIT) {
                if (a != m)
                    break;
                state = next = (s += WBIT) == 0L ? ORIGIN : s;
                if ((h = whead) != null && h.status != 0)
                    release(h);
                return next;
            }
            else if (a == 0L || a >= WBIT)
                break;
            else if (m < RFULL) {
                if (U.compareAndSwapLong(this, STATE, s, next = s - RUNIT)) {
                    if (m == RUNIT && (h = whead) != null && h.status != 0)
                        release(h);
                    return next & SBITS;
                }
            }
            else if ((next = tryDecReaderOverflow(s)) != 0L)
                return next & SBITS;
        }
        return 0L;
    }

    /**
     * Releases the write lock if it is held, without requiring a
     * stamp value. This method may be useful for recovery after
     * errors.
     *
     * @return {@code true} if the lock was held, else false
     */
    public boolean tryUnlockWrite() {
        long s; WNode h;
        if (((s = state) & WBIT) != 0L) {
            state = (s += WBIT) == 0L ? ORIGIN : s;
            if ((h = whead) != null && h.status != 0)
                release(h);
            return true;
        }
        return false;
    }

    /**
     * Releases one hold of the read lock if it is held, without
     * requiring a stamp value. This method may be useful for recovery
     * after errors.
     *
     * @return {@code true} if the read lock was held, else false
     */
    public boolean tryUnlockRead() {
        long s, m; WNode h;
        while ((m = (s = state) & ABITS) != 0L && m < WBIT) {
            if (m < RFULL) {
                if (U.compareAndSwapLong(this, STATE, s, s - RUNIT)) {
                    if (m == RUNIT && (h = whead) != null && h.status != 0)
                        release(h);
                    return true;
                }
            }
            else if (tryDecReaderOverflow(s) != 0L)
                return true;
        }
        return false;
    }

    // status monitoring methods

    /**
     * Returns combined state-held and overflow read count for given
     * state s.
     */
    private int getReadLockCount(long s) {
        long readers;
        if ((readers = s & RBITS) >= RFULL)
            readers = RFULL + readerOverflow;
        return (int) readers;
    }

    /**
     * Returns {@code true} if the lock is currently held exclusively.
     *
     * @return {@code true} if the lock is currently held exclusively
     */
    public boolean isWriteLocked() {
        return (state & WBIT) != 0L;
    }

    /**
     * Returns {@code true} if the lock is currently held non-exclusively.
     *
     * @return {@code true} if the lock is currently held non-exclusively
     */
    public boolean isReadLocked() {
        return (state & RBITS) != 0L;
    }

    /**
     * Queries the number of read locks held for this lock. This
     * method is designed for use in monitoring system state, not for
     * synchronization control.
     * @return the number of read locks held
     */
    public int getReadLockCount() {
        return getReadLockCount(state);
    }

    /**
     * Returns a string identifying this lock, as well as its lock
     * state.  The state, in brackets, includes the String {@code
     * "Unlocked"} or the String {@code "Write-locked"} or the String
     * {@code "Read-locks:"} followed by the current number of
     * read-locks held.
     *
     * @return a string identifying this lock, as well as its lock state
     */
    public String toString() {
        long s = state;
        return super.toString() +
                ((s & ABITS) == 0L ? "[Unlocked]" :
                        (s & WBIT) != 0L ? "[Write-locked]" :
                                "[Read-locks:" + getReadLockCount(s) + "]");
    }

    // views

    /**
     * Returns a plain {@link Lock} view of this StampedLock in which
     * the {@link Lock#lock} method is mapped to {@link #readLock},
     * and similarly for other methods. The returned Lock does not
     * support a {@link Condition}; method {@link
     * Lock#newCondition()} throws {@code
     * UnsupportedOperationException}.
     *
     * @return the lock
     */
    public Lock asReadLock() {
        ReadLockView v;
        return ((v = readLockView) != null ? v :
                (readLockView = new ReadLockView()));
    }

    /**
     * Returns a plain {@link Lock} view of this StampedLock in which
     * the {@link Lock#lock} method is mapped to {@link #writeLock},
     * and similarly for other methods. The returned Lock does not
     * support a {@link Condition}; method {@link
     * Lock#newCondition()} throws {@code
     * UnsupportedOperationException}.
     *
     * @return the lock
     */
    public Lock asWriteLock() {
        WriteLockView v;
        return ((v = writeLockView) != null ? v :
                (writeLockView = new WriteLockView()));
    }

    /**
     * Returns a {@link ReadWriteLock} view of this StampedLock in
     * which the {@link ReadWriteLock#readLock()} method is mapped to
     * {@link #asReadLock()}, and {@link ReadWriteLock#writeLock()} to
     * {@link #asWriteLock()}.
     *
     * @return the lock
     */
    public ReadWriteLock asReadWriteLock() {
        ReadWriteLockView v;
        return ((v = readWriteLockView) != null ? v :
                (readWriteLockView = new ReadWriteLockView()));
    }

    // view classes

    final class ReadLockView implements Lock {
        public void lock() { readLock(); }
        public void lockInterruptibly() throws InterruptedException {
            readLockInterruptibly();
        }
        public boolean tryLock() { return tryReadLock() != 0L; }
        public boolean tryLock(long time, TimeUnit unit)
                throws InterruptedException {
            return tryReadLock(time, unit) != 0L;
        }
        public void unlock() { unstampedUnlockRead(); }
        public Condition newCondition() {
            throw new UnsupportedOperationException();
        }
    }

    final class WriteLockView implements Lock {
        public void lock() { writeLock(); }
        public void lockInterruptibly() throws InterruptedException {
            writeLockInterruptibly();
        }
        public boolean tryLock() { return tryWriteLock() != 0L; }
        public boolean tryLock(long time, TimeUnit unit)
                throws InterruptedException {
            return tryWriteLock(time, unit) != 0L;
        }
        public void unlock() { unstampedUnlockWrite(); }
        public Condition newCondition() {
            throw new UnsupportedOperationException();
        }
    }

    final class ReadWriteLockView implements ReadWriteLock {
        public Lock readLock() { return asReadLock(); }
        public Lock writeLock() { return asWriteLock(); }
    }

    // Unlock methods without stamp argument checks for view classes.
    // Needed because view-class lock methods throw away stamps.

    final void unstampedUnlockWrite() {
        WNode h; long s;
        if (((s = state) & WBIT) == 0L)
            throw new IllegalMonitorStateException();
        state = (s += WBIT) == 0L ? ORIGIN : s;
        if ((h = whead) != null && h.status != 0)
            release(h);
    }

    final void unstampedUnlockRead() {
        for (;;) {
            long s, m; WNode h;
            if ((m = (s = state) & ABITS) == 0L || m >= WBIT)
                throw new IllegalMonitorStateException();
            else if (m < RFULL) {
                if (U.compareAndSwapLong(this, STATE, s, s - RUNIT)) {
                    if (m == RUNIT && (h = whead) != null && h.status != 0)
                        release(h);
                    break;
                }
            }
            else if (tryDecReaderOverflow(s) != 0L)
                break;
        }
    }

    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        state = ORIGIN; // reset to unlocked state
    }

    // internals

    /**
     * Tries to increment readerOverflow by first setting state
     * access bits value to RBITS, indicating hold of spinlock,
     * then updating, then releasing.
     *
     * @param s a reader overflow stamp: (s & ABITS) >= RFULL
     * @return new stamp on success, else zero
     */
    private long tryIncReaderOverflow(long s) {
        // 读线程数溢出

        // assert (s & ABITS) >= RFULL;
        if ((s & ABITS) == RFULL) {
            if (U.compareAndSwapLong(this, STATE, s, s | RBITS)) {
                ++readerOverflow;
                state = s;
                return s;
            }
        }
        else if ((LockSupport.nextSecondarySeed() &
                OVERFLOW_YIELD_RATE) == 0)
            Thread.yield();
        return 0L;
    }

    /**
     * Tries to decrement readerOverflow.
     *
     * @param s a reader overflow stamp: (s & ABITS) >= RFULL
     * @return new stamp on success, else zero
     */
    private long tryDecReaderOverflow(long s) {
        // assert (s & ABITS) >= RFULL;
        if ((s & ABITS) == RFULL) {
            if (U.compareAndSwapLong(this, STATE, s, s | RBITS)) {
                int r; long next;
                if ((r = readerOverflow) > 0) {
                    readerOverflow = r - 1;
                    next = s;
                }
                else
                    next = s - RUNIT;
                state = next;
                return next;
            }
        }
        else if ((LockSupport.nextSecondarySeed() &
                OVERFLOW_YIELD_RATE) == 0)
            Thread.yield();
        return 0L;
    }

    /**
     * 唤醒 h 结点的后续结点（h 通常表示等待结点的头结点）
     * Wakes up the successor of h (normally whead).
     * 简单来说，这个方法等效于 h.next，但是它可以在 h 的下一个指针有延迟的时候从尾结点遍历
     * This is normally just h.next, but may require traversal from wtail
     * if next pointers are lagging.
     * 当后续的一个或者多个线程结点被取消时，这个方法在唤醒后续线程的时候可能会失败
     * This may fail to wake up an acquiring thread when one or more
     * have been cancelled, but the cancel methods themselves provide
     * extra safeguards to ensure liveness.
     */
    private void release(WNode h) {
        if (h != null) {
            WNode q; Thread w;
            U.compareAndSwapInt(h, WSTATUS, WAITING, 0);// 如果 h 结点处于 WAITING 状态，将其状态设置为 0
            // q = h.next == null || q.status == CANCELLED 下一个结点 q 为 null 或者下一个结点中的线程已经被取消
            if ((q = h.next) == null || q.status == CANCELLED) {
                for (WNode t = wtail; t != null && t != h; t = t.prev) // 从尾结点开始逆向遍历，获取正在等待的线程
                    if (t.status <= 0)
                        q = t;
            }
            // q != null && q.thread != null 下一个结点 q 不为 null 并且结点中的线程仍然存活
            if (q != null && (w = q.thread) != null)
                U.unpark(w); // 唤醒下一个线程
        }
    }

    /**
     * See above for explanation.
     *
     * @param interruptible true if should check interrupts and if so
     * return INTERRUPTED
     * @param deadline if nonzero, the System.nanoTime value to timeout
     * at (and return zero)
     * @return next state, or INTERRUPTED
     */
    private long acquireWrite(boolean interruptible, long deadline) {
        // 初始化两个 wnode 节点，node 表示当前线程，p 表示头结点
        WNode node = null, p;
        for (int spins = -1;;) { // spin while enqueuing
            long m, s, ns;
            // state & ABITS == 0L 条件成立表示当前锁资源没有被别的线程占有
            // 可以直接获取
            if ((m = (s = state) & ABITS) == 0L) {
                if (U.compareAndSwapLong(this, STATE, s, ns = s + WBIT))
                    return ns; // 获取成功返回 stamp，退出自旋循环
            }
            else if (spins < 0) // spins < 0 初始化 spins，设置当前线程最大自旋次数 SPINS= 64
                spins = (m == WBIT && wtail == whead) ? SPINS : 0;
            else if (spins > 0) {
                // 获取一个伪随机数
                if (LockSupport.nextSecondarySeed() >= 0) // 获取伪随机数，如果大于 0 则 --spins
                    --spins;
            }
            // spins 自减到 0 也没能获取到锁，进入下一个 else if
            // p = wtail == null 队列尾结点为空，表示此时队列为空队列，开始初始化队列
            else if ((p = wtail) == null) { // initialize queue
                // 初始化写模式头结点
                WNode hd = new WNode(WMODE, null);
                // CAS 操作将 hd 设置到对象内存中
                if (U.compareAndSwapObject(this, WHEAD, null, hd))
                    wtail = hd; // 此时队列中只有一个结点，因此尾结点=头结点
            }
            else if (node == null) // 如果 node 为 null，初始化
                // 初始化写模式的 wnode 结点，前驱结点为头结点 p
                node = new WNode(WMODE, p);
            else if (node.prev != p) // 如果 node 不为 null
                node.prev = p; // node 的前驱结点设置为头结点 p
            else if (U.compareAndSwapObject(this, WTAIL, p, node)) {
                p.next = node; // 将头结点的后继结点设置为 node
                break; // node 结点成功进入等待队列，退出循环
            }
        }

        // 若只有一个结点在等待获取锁，结点示意图： p <--> node，p 和 node 相互指向

        // 到这一步的线程有两种情况：
        // 1、获取到了写锁
        // 2、进入等待队列

        // 到这一步的结点 p 情况如下：
        // 1、这是第一次初始化队列，p = whead = wtail，此时队列中只有一个结点
        // 2、队列已经存在，node 此时是尾结点 node=wtail，p是 node 的前驱，此时队列中有多个结点

        for (int spins = -1;;) {
            // 初始化一系列 wnode 结点供后续使用
            WNode h, np, pp; int ps;
            if ((h = whead) == p) { // 头结点存在，且为 p，此时队列中只有一个结点
                if (spins < 0)
                    // HEAD_SPINS 表示头结点最大自旋次数，HEAD_SPINS 和 CPU 核心数有关
                    spins = HEAD_SPINS; // 假设是多核 CPU，HEAD_SPINS=2^10=1024，否则 HEAD_SPINS=0
                else if (spins < MAX_HEAD_SPINS) // 多核CPU，MAX_HEAD_SPINS=2^16=65536，否则 MAX_HEAD_SPINS=0
                    spins <<= 1; // spins = spins << 1 = spins * 2
                for (int k = spins;;) { // spin at head 头结点自旋
                    long s, ns;
                    // 尝试获取锁
                    if (((s = state) & ABITS) == 0L) { // 获取成功
                        if (U.compareAndSwapLong(this, STATE, s,
                                ns = s + WBIT)) {
                            whead = node; // 原先的头结点获取成功，到下一个结点尝试获取
                            node.prev = null;
                            return ns;
                        }
                    }
                    else if (LockSupport.nextSecondarySeed() >= 0 && // 获取失败，继续自旋
                            --k <= 0)
                        break;
                }
            }
            // p 不是头结点，队列中存在多个结点
            else if (h != null) { // help release stale waiters 帮助唤醒链表中的其他等待结点
                WNode c; Thread w;
                // c = h.cowait，c 表示等待获取锁的结点链表
                while ((c = h.cowait) != null) { // 等待链表非空，循环唤醒链表中的结点
                    if (U.compareAndSwapObject(h, WCOWAIT, c, c.cowait) &&
                            (w = c.thread) != null)
                        U.unpark(w); // 唤醒结点线程
                }
            }

            // 到这里结点的有两种情况：
            // 1、只存在一个结点。头结点一直自旋，并且已经达到头结点自旋上限，获取锁失败
            // 2、存在多个结点。已经循环唤醒等待队列中的结点

            // 到这一步的结点 p 情况如下：
            // 1、这是第一次初始化队列，p = whead = wtail，此时队列中只有一个结点
            // 2、队列已经存在，node 此时是尾结点 node=wtail，p是 node 的前驱，此时队列中有多个结点

            // 若只有一个结点在等待获取锁，结点示意图： p <--> node

            if (whead == h) { // 存在头结点且为 h
                // np 表示 node 结点的前驱结点
                if ((np = node.prev) != p) {
                    if (np != null)
                        // 将 node 放到链表末尾
                        (p = np).next = node;   // stale
                }
                // 此时 p 结点仍然是头结点，将 p 结点的状态设置为 WAITING = -1
                else if ((ps = p.status) == 0)
                    U.compareAndSwapInt(p, WSTATUS, 0, WAITING);
                else if (ps == CANCELLED) { // 如果 p 结点状态为 CANCELLED = 1
                    // 将 node 结点和 p 的前驱结点 pp 相连接
                    if ((pp = p.prev) != null) {
                        node.prev = pp;
                        pp.next = node;
                    }
                }
                // 走到这里说明头结点 p 状态已经是 WAITING
                else {
                    long time; // 0 argument to park means no timeout
                    // deadline = 0
                    if (deadline == 0L)
                        time = 0L;
                    // deadline 已经过期，取消等待
                    else if ((time = deadline - System.nanoTime()) <= 0L)
                        return cancelWaiter(node, node, false);
                    // deadline !=0 且未过期
                    // 获取当前线程
                    Thread wt = Thread.currentThread();
                    // 保存对 wt 对象的引用
                    U.putObject(wt, PARKBLOCKER, this);
                    // 将当前线程保存到 node.thread
                    node.thread = wt;
                    // p != h ==> p 不是头结点
                    // node 的前驱结点是 p 结点
                    // whead == h ==> h 是头结点
                    // p.status < 0 说明 p 结点在 WAITING 状态
                    // state & ABITS != 0L 表示目前的锁已经被其他线程占有
                    if (p.status < 0 && (p != h || (state & ABITS) != 0L) &&
                            whead == h && node.prev == p)
                        // 进入这一步说明等待队列中存在多个等待结点

                        // UnSafe.park 模拟 LockSupport.park，实际上  LockSupport.park 底层实现就是依靠 UnSafe.park
                        /**
                         * 阻塞当前线程，当 balancing unpark 发生或 balancing unpark 已经发生时恢复
                         * Block current thread, returning when a balancing
                         * unpark occurs, or a balancing unpark has already occurred,
                         * 或者线程被中断后恢复
                         * or the thread is interrupted,
                         * 如果 absolute=false 并且 time 非零，等待 time 时间后恢复
                         * or, if not absolute and time is not zero, the given time nanoseconds have elapsed,
                         * 如果 absolute=true 并且 time 非零；或者传过来的参数 time 已经过期，线程恢复
                         * or if absolute, the given deadline in milliseconds since Epoch has passed,
                         * 或者线程虚假恢复
                         * or spuriously (i.e., returning for no
                         * "reason").
                         * Note: This operation is in the Unsafe class only
                         * because unpark is, so it would be strange to place it
                         * elsewhere.
                         */
                        // park 当前线程，当前线程进入等待状态，并等待被唤醒
                        U.park(false, time);  // emulate LockSupport.park

                    // 到这里说明线程被唤醒，将 node.thread 设置为 null
                    // 因为 node 结点是 dummy 结点，因此不会对线程有影响
                    node.thread = null;
                    // 清空对 wt 对象的引用
                    U.putObject(wt, PARKBLOCKER, null);
                    if (interruptible && Thread.interrupted())
                        // 如果线程可中断或者已经被中断，取消等待
                        return cancelWaiter(node, node, true);
                }
            }
        }
    }

    /**
     * See above for explanation.
     *
     * @param interruptible true if should check interrupts and if so
     * return INTERRUPTED
     * @param deadline if nonzero, the System.nanoTime value to timeout
     * at (and return zero)
     * @return next state, or INTERRUPTED
     */
    private long acquireRead(boolean interruptible, long deadline) {
        // 假设已经有第一个获取到读锁的线程，且目前读锁被持有
        // state=513
        WNode node = null, p;
        for (int spins = -1;;) { // 当前结点自旋循环
            WNode h;
            // 假设已经有第一个获取到读锁的线程，且目前读锁被持有
            // whead = whead 说明队列为空
            if ((h = whead) == (p = whead)) {
                for (long m, s, ns;;) {
                    /**
                     * 尝试获取读锁
                     * ABITS=255，RFULL=126 表示最大读线程数
                     * 存在写锁 s=state=384，state & ABITS = 128 > RFULL=126
                     * 写锁已经释放 s=state=512，state & ABITS = 0 < RFULL=126
                     */
                    if ((m = (s = state) & ABITS) < RFULL ?
                            U.compareAndSwapLong(this, STATE, s, ns = s + RUNIT) : // 写锁已经释放，尝试获取读锁
                            /**
                             * m < WBIT=128 表示当前存在读锁
                             * 写锁未释放，当前是读模式，且读线程队列溢出
                             * 使用 readerOverflow 来保存溢出的读线程
                             */
                            (m < WBIT && (ns = tryIncReaderOverflow(s)) != 0L)) // m < WBIT 表示存在读锁
                        return ns;
                    else if (m >= WBIT) { // m >= WBIT 说明当前存在写锁，开始自旋
                        if (spins > 0) {
                            // LockSupport.nextSecondarySeed() 获取一个伪随机数，可能大于等于 0，也可能小于 0
                            // 只有获取到的伪随机数大于等于0的时候才会进行 --spins
                            if (LockSupport.nextSecondarySeed() >= 0)
                                --spins;
                        }
                        else {
                            if (spins == 0) { // 自旋到 SPINS 次还是没能获取读锁
                                /**
                                 * 判断头结点是否发生变化
                                 * 简单来说就是当前结点一直在自旋获取锁，但是都没有获取到，因为锁被这个线程之前的别的读或者写线程获取到了。
                                 * 当前线程经过漫长时间的自旋也没能获取到锁，就开始检查等待链表中的头结点是否发生变化，先前获取到锁的头结点线程是否已经释放锁。
                                 */
                                WNode nh = whead, np = wtail;
                                // nh == h && np == p 表示头尾结点都没发生变化
                                // (h = nh) != (p = np) 头尾结点发生了变化，说明队列中新增了别的结点
                                if ((nh == h && np == p) || (h = nh) != (p = np))
                                    break; // 退出自旋
                            }
                            spins = SPINS;
                        }
                    }
                }
            }

            /**
             * 到这里说明读结点经过至少 spins 次数的自旋还是没能获取到锁
             * 此时 wnode 结点可能的情况如下：
             * 1、只存在当前一个结点，头尾结点未发生变化
             * 2、新增了别的结点，并且这些结点组成了等待队列，头或尾结点发生了变化
             */

            // 如果头结点为空，说明未形成队列，开始初始化读等待队列
            if (p == null) { // initialize queue
                WNode hd = new WNode(WMODE, null);
                // 将头结点设置到当前对象的 WHEAD 位置
                if (U.compareAndSwapObject(this, WHEAD, null, hd))
                    wtail = hd; // 尾结点=头结点，说明此时只有一个结点
            }
            // 如果头结点不为空，说明等待队列已存在
            else if (node == null)
                // 初始化读模式的 node 结点
                node = new WNode(RMODE, p);
            // h == p 头结点未发生变化
            // 或者 p.mode != RMODE 头结点不是读结点
            else if (h == p || p.mode != RMODE) { // p.mode != RMODE 表示当前存在写锁
                if (node.prev != p)
                    node.prev = p; // 将 node 结点挂载到头结点后
                else if (U.compareAndSwapObject(this, WTAIL, p, node)) {
                    p.next = node; // 将 node 设置为尾结点
                    break; // 跳出自旋循环
                }
            }
            else if (!U.compareAndSwapObject(p, WCOWAIT,
                    node.cowait = p.cowait, node))
                node.cowait = null;
            else {
                for (;;) {
                    WNode pp, c; Thread w;
                    if ((h = whead) != null && (c = h.cowait) != null &&
                            U.compareAndSwapObject(h, WCOWAIT, c, c.cowait) &&
                            (w = c.thread) != null) // help release
                        U.unpark(w);
                    if (h == (pp = p.prev) || h == p || pp == null) {
                        long m, s, ns;
                        do {
                            if ((m = (s = state) & ABITS) < RFULL ?
                                    U.compareAndSwapLong(this, STATE, s,
                                            ns = s + RUNIT) :
                                    (m < WBIT &&
                                            (ns = tryIncReaderOverflow(s)) != 0L))
                                return ns;
                        } while (m < WBIT);
                    }
                    if (whead == h && p.prev == pp) {
                        long time;
                        if (pp == null || h == p || p.status > 0) {
                            node = null; // throw away
                            break;
                        }
                        if (deadline == 0L)
                            time = 0L;
                        else if ((time = deadline - System.nanoTime()) <= 0L)
                            return cancelWaiter(node, p, false);
                        Thread wt = Thread.currentThread();
                        U.putObject(wt, PARKBLOCKER, this);
                        node.thread = wt;
                        if ((h != pp || (state & ABITS) == WBIT) &&
                                whead == h && p.prev == pp)
                            U.park(false, time);
                        node.thread = null;
                        U.putObject(wt, PARKBLOCKER, null);
                        if (interruptible && Thread.interrupted())
                            return cancelWaiter(node, p, true);
                    }
                }
            }
        }

        /**
         * 上一个自旋循环是当前结点自旋，到这里开始下一个自旋循环：头结点自旋
         * 和写线程获取锁的逻辑相似
         */

        for (int spins = -1;;) { // 头结点自旋循环
            WNode h, np, pp; int ps;
            if ((h = whead) == p) {
                if (spins < 0)
                    spins = HEAD_SPINS; // HEAD_SPINS 和 CPU 核心数相关，多核循环 2^10 次，单核 0 次
                else if (spins < MAX_HEAD_SPINS)
                    spins <<= 1; // 每次 spins 增大到自己的 2 倍
                for (int k = spins;;) { // spin at head
                    long m, s, ns;
                    // 尝试获取锁，获取失败判断读线程数是否溢出
                    if ((m = (s = state) & ABITS) < RFULL ?
                            U.compareAndSwapLong(this, STATE, s, ns = s + RUNIT) :
                            // m < WBIT=128 表示当前存在读锁
                            (m < WBIT && (ns = tryIncReaderOverflow(s)) != 0L)) {
                        WNode c; Thread w;
                        whead = node;
                        node.prev = null;
                        while ((c = node.cowait) != null) {
                            if (U.compareAndSwapObject(node, WCOWAIT,
                                    c, c.cowait) &&
                                    (w = c.thread) != null)
                                U.unpark(w);
                        }
                        return ns;
                    }
                    // m >= WBIT 表示当前存在写锁
                    else if (m >= WBIT &&
                            LockSupport.nextSecondarySeed() >= 0 && --k <= 0)
                        break;
                }
            }
            else if (h != null) { // 头结点非空，循环唤醒队列中的线程
                WNode c; Thread w;
                while ((c = h.cowait) != null) {
                    if (U.compareAndSwapObject(h, WCOWAIT, c, c.cowait) &&
                            (w = c.thread) != null)
                        U.unpark(w);
                }
            }
            if (whead == h) { // 头结点经过自旋仍然未能获取到锁
                if ((np = node.prev) != p) { // 如果当前 node 的前驱不是头结点
                    if (np != null) // 并且前驱非空
                        // 将当前结点放入等待链表末尾
                        (p = np).next = node;   // stale
                }
                else if ((ps = p.status) == 0)
                    U.compareAndSwapInt(p, WSTATUS, 0, WAITING); // 自旋获取锁不成功，将头结点状态设置为 WAITING=-1
                else if (ps == CANCELLED) { // 如果头结点处于 CANCELLED=1 状态时，node 结点向前
                    if ((pp = p.prev) != null) {
                        node.prev = pp;
                        pp.next = node;
                    }
                }
                // 下面的逻辑和 acquireWriteLock 相同
                else {
                    long time;
                    if (deadline == 0L)
                        time = 0L;
                    else if ((time = deadline - System.nanoTime()) <= 0L)
                        return cancelWaiter(node, node, false);
                    Thread wt = Thread.currentThread();
                    U.putObject(wt, PARKBLOCKER, this);
                    node.thread = wt;
                    if (p.status < 0 &&
                            (p != h || (state & ABITS) == WBIT) &&
                            whead == h && node.prev == p)
                        U.park(false, time);
                    node.thread = null;
                    U.putObject(wt, PARKBLOCKER, null);
                    if (interruptible && Thread.interrupted())
                        return cancelWaiter(node, node, true);
                }
            }
        }
    }

    /**
     * If node non-null, forces cancel status and unsplices it from
     * queue if possible and wakes up any cowaiters (of the node, or
     * group, as applicable), and in any case helps release current
     * first waiter if lock is free. (Calling with null arguments
     * serves as a conditional form of release, which is not currently
     * needed but may be needed under possible future cancellation
     * policies). This is a variant of cancellation methods in
     * AbstractQueuedSynchronizer (see its detailed explanation in AQS
     * internal documentation).
     *
     * @param node if nonnull, the waiter
     * @param group either node or the group node is cowaiting with
     * @param interrupted if already interrupted
     * @return INTERRUPTED if interrupted or Thread.interrupted, else zero
     */
    private long cancelWaiter(WNode node, WNode group, boolean interrupted) {
        if (node != null && group != null) {
            Thread w;
            node.status = CANCELLED;
            // unsplice cancelled nodes from group
            for (WNode p = group, q; (q = p.cowait) != null;) {
                if (q.status == CANCELLED) {
                    U.compareAndSwapObject(p, WCOWAIT, q, q.cowait);
                    p = group; // restart
                }
                else
                    p = q;
            }
            if (group == node) {
                for (WNode r = group.cowait; r != null; r = r.cowait) {
                    if ((w = r.thread) != null)
                        U.unpark(w);       // wake up uncancelled co-waiters
                }
                for (WNode pred = node.prev; pred != null; ) { // unsplice
                    WNode succ, pp;        // find valid successor
                    while ((succ = node.next) == null ||
                            succ.status == CANCELLED) {
                        WNode q = null;    // find successor the slow way
                        for (WNode t = wtail; t != null && t != node; t = t.prev)
                            if (t.status != CANCELLED)
                                q = t;     // don't link if succ cancelled
                        if (succ == q ||   // ensure accurate successor
                                U.compareAndSwapObject(node, WNEXT,
                                        succ, succ = q)) {
                            if (succ == null && node == wtail)
                                U.compareAndSwapObject(this, WTAIL, node, pred);
                            break;
                        }
                    }
                    if (pred.next == node) // unsplice pred link
                        U.compareAndSwapObject(pred, WNEXT, node, succ);
                    if (succ != null && (w = succ.thread) != null) {
                        succ.thread = null;
                        U.unpark(w);       // wake up succ to observe new pred
                    }
                    if (pred.status != CANCELLED || (pp = pred.prev) == null)
                        break;
                    node.prev = pp;        // repeat if new pred wrong/cancelled
                    U.compareAndSwapObject(pp, WNEXT, pred, succ);
                    pred = pp;
                }
            }
        }
        WNode h; // Possibly release first waiter
        while ((h = whead) != null) {
            long s; WNode q; // similar to release() but check eligibility
            if ((q = h.next) == null || q.status == CANCELLED) {
                for (WNode t = wtail; t != null && t != h; t = t.prev)
                    if (t.status <= 0)
                        q = t;
            }
            if (h == whead) {
                if (q != null && h.status == 0 &&
                        ((s = state) & ABITS) != WBIT && // waiter is eligible
                        (s == 0L || q.mode == RMODE))
                    release(h);
                break;
            }
        }
        return (interrupted || Thread.interrupted()) ? INTERRUPTED : 0L;
    }

    // Unsafe mechanics
    private static final sun.misc.Unsafe U;
    private static final long STATE; // 16
    private static final long WHEAD; // 24
    private static final long WTAIL; // 28
    private static final long WNEXT; // 24
    private static final long WSTATUS; // 12
    private static final long WCOWAIT; // 28
    private static final long PARKBLOCKER; // 88

    static {
        try {
            U = sun.misc.Unsafe.getUnsafe();
            Class<?> k = StampedLock.class;
            Class<?> wk = WNode.class;
            STATE = U.objectFieldOffset
                    (k.getDeclaredField("state"));
            WHEAD = U.objectFieldOffset
                    (k.getDeclaredField("whead"));
            WTAIL = U.objectFieldOffset
                    (k.getDeclaredField("wtail"));
            WSTATUS = U.objectFieldOffset
                    (wk.getDeclaredField("status"));
            WNEXT = U.objectFieldOffset
                    (wk.getDeclaredField("next"));
            WCOWAIT = U.objectFieldOffset
                    (wk.getDeclaredField("cowait"));
            Class<?> tk = Thread.class;
            PARKBLOCKER = U.objectFieldOffset
                    (tk.getDeclaredField("parkBlocker"));

        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
