package juc.debug;

import java.util.concurrent.locks.StampedLock;

/**
 * Two threads contend the lock
 *
 * @author gnl
 * @since 2023/2/9 下午2:45
 */
public class StampedLockReadLockDebug {

    private static final StampedLock sl = new StampedLock();

    public static volatile int count = 0;

    public static void doSomething() {
        int step = 1;
        long stamp = sl.writeLock();
        try {
            count += step;
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            sl.unlockWrite(stamp);
        }
    }

    public static void testReadLock() {
        int state;
        long stamp = sl.readLock();
        try {
            state = count;
            Thread.sleep(1000);
            System.out.println(state);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            sl.unlockRead(stamp);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread a = new Thread(() -> {
            doSomething();
        }, "a");

        Thread b = new Thread(() -> {
            testReadLock();
        }, "b");

        Thread c = new Thread(() -> {
            testReadLock();
        }, "c");

        // the default priority is 5
        a.setPriority(6);
        a.start();
        Thread.sleep(100);
        b.start();
         Thread.sleep(10);
         c.start();
    }
}
