package juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

/**
 * 分段锁，所有线程一起到达某一个阶段之后才能继续下一步
 */
public class PhaserTest {
    public static void main(String[] args) {
//        final Phaser phaser = new Phaser(1);
//        System.out.println("init phaser " + phaser.getPhase());
//        for (int i = 0; i < 5; i++) {
//            int finalI = i;
//            phaser.register();
//            new Thread(() -> {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                int arrive = phaser.arriveAndAwaitAdvance();
//                System.out.println(finalI + " done arrive " + arrive);
//            }).start();
//            System.out.println("register phaser " + phaser.getPhase());
//        }
//        System.out.println("after phaser " + phaser.arriveAndDeregister());


        List<Runnable> tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            tasks.add(() -> {
                System.out.println("xxx-" + finalI);
            });
        }

        PhaserTest test = new PhaserTest();
        test.runTasks(tasks);
    }

    void runTasks(List<Runnable> tasks) {
        final Phaser phaser = new Phaser(1);
        // create and start threads
        for (final Runnable task : tasks) {
            phaser.register();
            new Thread() {
                public void run() {
                    phaser.arriveAndAwaitAdvance(); // await all creation
                    task.run();
                }
            }.start();
        }
        // allow threads to start and deregister self
        phaser.arriveAndDeregister();
    }
}
