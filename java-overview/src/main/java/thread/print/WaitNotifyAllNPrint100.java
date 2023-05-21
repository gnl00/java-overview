package thread.print;

/**
 * N 个线程交替打印 1-100
 *
 * @author gnl
 * @since 2023/2/4
 */
public class WaitNotifyAllNPrint100 {

    private static int num = 1;
    private static final Object MONITOR = new Object();

    public static void print(int target) {
        synchronized (MONITOR) {
            while (num <= 100) {
                while (num % 5 == target) {
                    System.out.println(Thread.currentThread().getName() + " ==> " + num++);
                }
                MONITOR.notifyAll();
                try {
                    MONITOR.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            MONITOR.notifyAll();
        }
    }

    public static void main(String[] args) {
        new Thread(() -> {
            print(1);
        }, "a").start();

        new Thread(() -> {
            print(2);
        }, "b").start();

        new Thread(() -> {
            print(3);
        }, "c").start();

        new Thread(() -> {
            print(4);
        }, "d").start();

        new Thread(() -> {
            print(0);
        }, "e").start();
    }
}
