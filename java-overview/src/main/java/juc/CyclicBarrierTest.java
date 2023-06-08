package juc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 集齐 7 颗龙珠
 */
public class CyclicBarrierTest {
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(7, () -> {
            System.out.println("All works done");
        });
        for (int i = 0; i < 7; i++) {
            new Thread(() -> {

                System.out.println(Thread.currentThread().getName() + " ==> DONE ");

                try {
                    barrier.await(); // 当前线程工作完成，进入等待
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }

            }, "th-" + i).start();
        }
    }
}
