package thread.print;

import java.util.concurrent.locks.LockSupport;

/**
 * LockSupport implementation
 *
 * @author gnl
 * @since 2023/2/6 下午12:59
 */
public class LockSupportPrint {
    private static int num = 0;
    private static Thread a, b ,c = null;
    public static void print(int target, Thread next) {
        for (int i = 0; i < 10;) {
            try {
                while (num % 3 == target) {
                    System.out.println(Thread.currentThread().getName() + ": " + i);
                    num++;
                    i++;
                }
                LockSupport.unpark(next);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                // 防止最后一轮循环造成死锁
                if (i < 10) {
                    LockSupport.park();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        a = new Thread(() -> {
            print(0, b);
        }, "a");

        b = new Thread(() -> {
            LockSupport.park();
            print(1, c);
        }, "b");

        c = new Thread(() -> {
            LockSupport.park();
            print(2, a);
        }, "c");

        a.start();
        b.start();
        c.start();
    }
}