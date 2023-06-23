package thread.print;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock and Condition 更加精确的利用锁控制共享资源
 *
 * @author gnl
 * @since 2023/2/5 下午9:02
 */
public class LockConditionPrint {
    private static Lock lock = new ReentrantLock();
    private static Condition c1 = lock.newCondition();
    private static Condition c2 = lock.newCondition();
    private static Condition c3 = lock.newCondition();

    private static int num = 0;
    public static void print(int target, Condition current, Condition next) {
        for (int i = 0; i < 10; i++) {
            lock.lock();
            try {
                while (num % 3 != target) {
                    current.await(); // 不是目标线程，await
                }
                System.out.println(Thread.currentThread().getName());
                num++;
                next.signal(); // 唤醒下一个线程
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }
    public static void main(String[] args) {
        new Thread(() -> {
            print(0, c1, c2);
        }, "a").start();

        new Thread(() -> {
            print(1, c2, c3);
        }, "b").start();

        new Thread(() -> {
            print(2, c3, c1);
        }, "c").start();
    }
}
