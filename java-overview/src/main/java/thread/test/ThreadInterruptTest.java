package thread.test;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/2/8 下午1:34
 */
public class ThreadInterruptTest {

    public static void print() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(i);
            if (i == 5) Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        new Thread(() -> {
            print();
            System.out.println(Thread.currentThread().isInterrupted());
        }, "A").start();
    }
}
