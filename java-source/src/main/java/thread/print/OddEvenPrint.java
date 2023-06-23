package thread.print;

/**
 * 2 个线程交替打印 1-100 奇偶数
 *
 * @author gnl
 * @since 2023/2/4
 */
public class OddEvenPrint {
    private static int num = 1;
    private static final Object MONITOR = new Object();

    public static void printOddEven() {
        synchronized (MONITOR) {
            while (num < 100) {
                System.out.println(Thread.currentThread().getName() + ": " + num++);
                // 先唤醒其他线程
                MONITOR.notify();
                try {
                    // 再 wait
                    MONITOR.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            MONITOR.notify();
        }
    }

    public static void main(String[] args) {
        new Thread(() -> {
            printOddEven();
        }, "A").start();
        new Thread(() -> {
            printOddEven();
        }, "B").start();
    }
}
