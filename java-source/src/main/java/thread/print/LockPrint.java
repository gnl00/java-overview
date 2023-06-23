package thread.print;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用 Lock 来控制交替打印
 *
 * @author gnl
 * @since 2023/2/4
 */
public class LockPrint {
    private static int num = 0;
    private static Lock lock = new ReentrantLock();

    public static void print(int target) {
        for (int i = 0; i < 10;) {
            lock.lock();
            try {
                while (num % 3 == target) {
                    System.out.println(Thread.currentThread().getName());
                    num++;
                    i++;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        new Thread(() -> {
            print(0);
        },"a").start();

        new Thread(() -> {
            print(1);
        },"b").start();

        new Thread(() -> {
            print(2);
        },"c").start();
    }
}
