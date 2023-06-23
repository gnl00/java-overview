package thread.print;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch implementation
 */
public class CountDownLatchPrint {
    private static CountDownLatch counter = new CountDownLatch(10);
    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= 10; i++) {
            Thread.sleep(1000);
            System.out.println("leaves count: " + i);
            counter.countDown();
        }

        counter.await();
        lock();
    }

    public static void lock() {
        System.out.println("lock the door");
    }
}
