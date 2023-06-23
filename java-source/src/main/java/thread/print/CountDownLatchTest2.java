package thread.print;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch implementation. Split a problem into N parts and resolve
 *
 * @author gnl
 */
public class CountDownLatchTest2 {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch doneSignal = new CountDownLatch(1);

        for (int i = 0; i < 10; i++) {
            new Thread(new Resolver(doneSignal, i)).start();
        }

        doneSignal.await(); // wait for thr result
    }
}

class Resolver implements Runnable {
    private CountDownLatch doneSignal;
    private int problem;

    public Resolver(CountDownLatch doneSignal, int problem) {
        this.doneSignal = doneSignal;
        this.problem = problem;
    }

    @Override
    public void run() {
        try {
            doWork();
            doneSignal.countDown();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void doWork() {
        System.out.println("Calculating: " + problem);
    }
}
