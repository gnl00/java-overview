package thread.print;

/**
 * wait and notifyAll test
 *
 * @author gnl
 * @since 2023/2/6
 */
public class WaitNotifyAllPrint1A2B3C {
    private static int num = 0;
    private static final Object LOCK = new Object();

    public static void print(int target, int n) {
        synchronized (LOCK) {
            while (num % 3 != target) {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            for (int i = 0; i < n; i++) {
                System.out.print(Thread.currentThread().getName());
            }
            num++;
            LOCK.notifyAll();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread a = new Thread(() -> {
            print(0, 1);
        }, "A");

        Thread b = new Thread(() -> {
            print(1, 2);
        }, "B");

        Thread c = new Thread(() -> {
            print(2, 3);
        }, "C");

        a.start();
        b.start();
        c.start();

        a.join();
        b.join();
        c.join();

        System.out.println();
    }
}
