package thread.print;

import java.util.concurrent.CountDownLatch;

/**
 * 存在 A、B、C、D 四个线程
 * D 线程需要等到 A、B、C 执行完成之后才能执行
 */
public class CountDownLatchTest3 {
    public static void main(String[] args) {
        int count = 3;
        CountDownLatch cdl = new CountDownLatch(count);

        Thread a = new Thread(() -> {
            System.out.println("thread a start");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("thread a done");
            cdl.countDown();
        }, "A");

        Thread b = new Thread(() -> {
            System.out.println("thread b start");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("thread b done");
            cdl.countDown();
        }, "B");

        Thread c = new Thread(() -> {
            System.out.println("thread c start");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("thread c done");
            cdl.countDown();
        }, "C");

        Thread d = new Thread(() -> {
            System.out.println("thread d start");
        }, "D");

        a.start();
        b.start();
        c.start();

        try {
            cdl.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Thread D start
        d.start();

    }
}
