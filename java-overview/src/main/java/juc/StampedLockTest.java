package juc;

import java.util.concurrent.locks.StampedLock;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/2/8 下午1:51
 */
public class StampedLockTest {

    private static int state = 0;
    private static final StampedLock sl = new StampedLock();

    public static void setState() {
        long stamp = sl.writeLock();
        try {
            state = state + 1;
            System.out.println( Thread.currentThread().getName() + " " + stamp + " Write operation complete, state now: " + state);
            Thread.sleep(1000 * 60 * 30);

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            sl.unlockWrite(stamp);
        }
    }

    public static int getState() {
        // 通过 tryOptimisticRead() 获取一个乐观 stamp
        // tryOptimisticRead() 是乐观读，没有加锁，所以不用解锁/
        // 若存在写锁，返回 0
        long stamp = sl.tryOptimisticRead();
        int currentState = state;

        System.out.println(Thread.currentThread().getName() + stamp + " Check ");
        if (!sl.validate(stamp)) { // 检查在读取后是否有其他写锁发生
            System.out.println(Thread.currentThread().getName() + stamp + " detected write operation(s) HAPPENED ");

            stamp = sl.tryReadLock(); // 如果有写操作发生过，获取读锁，此时才需要真正解锁
            try {
                currentState = state;
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                sl.unlockRead(stamp);
            }
        }
        System.out.println(Thread.currentThread().getName() + stamp + " getting state: " + currentState);
        return currentState;
    }

    public static void main(String[] args) {
//        for (int i = 0; i < 10; i++) {
//            new Thread(() -> {
//                while (true) {
//                    getState();
//                    try {
//                        Thread.sleep(10);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                    setState();
//                }
//            }).start();
//        }

        new Thread(() -> {
            setState();
        }, "b").start();

        new Thread(() -> {
            setState();
        }, "c").start();
//
//        new Thread(() -> {
//            while (true) {
//                getState();
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                setState();
//            }
//        }, "d").start();
//
//        new Thread(() -> {
//            while (true) {
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                getState();
//                setState();
//            }
//        }, "e").start();
    }
}
