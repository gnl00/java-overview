package thread.print;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch implementation. All threads wait for thr startSignal to start
 *
 * @author gnl
 */
public class CountDownLatchTest {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            new Thread(new Worker(startSignal, doneSignal)).start();
        }

        // do something, such as initialization
        startSignal.countDown();
        // do something, such as getting results
        doneSignal.await(); // wait for all done
        // do something, such as cleanup
    }
}

class Worker implements Runnable {
    private static int workCount;
    private final CountDownLatch startSignal;
    private final CountDownLatch doneSignal;

    public Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
        this.startSignal = startSignal;
        this.doneSignal = doneSignal;
    }

    @Override
    public void run() {
        try {
            startSignal.await(); // wait for start signal
            doWork();
            doneSignal.countDown(); // count down after work finished
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void doWork() {
        System.out.println("workCount: " + workCount++);
    }
}
