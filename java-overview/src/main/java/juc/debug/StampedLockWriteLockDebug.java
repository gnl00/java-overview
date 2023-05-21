package juc.debug;

import java.util.concurrent.locks.StampedLock;

/**
 * Two threads contend the write-lock
 *
 * @author gnl
 * @since 2023/2/12
 */
public class StampedLockWriteLockDebug {
    private static final StampedLock sl = new StampedLock();

    public static volatile int count;

    public static void doSomething() {
        long stamp = sl.writeLock();
        try {
            System.out.println(count++);
            Thread.sleep(1000 * 60 * 30);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            sl.unlockWrite(stamp);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread a = new Thread(() -> {
            doSomething();
        }, "a");

        Thread b = new Thread(() -> {
            doSomething();
        }, "b");

        a.start();
        Thread.sleep(100); // make sure b runs after a
        b.start();
    }
}
