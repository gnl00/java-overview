package thread.print;

/**
 * 3 个线程交替打印 ABC
 *
 * @author gnl
 * @since 2023/2/4
 */
public class WaitNotifyPrintABC {
    private static int num = 0;
    private static final Object MONITOR = new Object();
    public static void printABC(int targetNum) {
        for (int i = 0; i < 10; i++) {
            synchronized (MONITOR) {
                while (num % 3 != targetNum) {
                    try {
                        MONITOR.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.print(Thread.currentThread().getName());
                num++;
                MONITOR.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        Thread a = new Thread(() -> {
            printABC(0);
        }, "A");
        Thread b = new Thread(() -> {
            printABC(1);
        }, "B");
        Thread c = new Thread(() -> {
            printABC(2);
        }, "C");

        a.start();
        b.start();
        c.start();
    }
}
