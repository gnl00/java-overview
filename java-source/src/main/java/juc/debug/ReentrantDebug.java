package juc.debug;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 锁重入 debug
 *
 * @author gnl
 * @since 2023/2/8
 */
public class ReentrantDebug {

    private static final ReentrantLock lock = new ReentrantLock();

    public static void doSomething() {
        lock.lock();
        try {
            System.out.println("outer");
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " is here ");
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        new Thread(() -> {
            doSomething();
        }, "a").start();
    }
}
