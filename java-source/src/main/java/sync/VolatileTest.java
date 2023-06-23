package sync;

/**
 * VolatileTest
 *
 * @author gnl
 * @since 2023/2/9 下午1:51
 */
public class VolatileTest {
    public static volatile int count = 0;

    public static void incrementCount() {
        count++; //非原子操作
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    incrementCount();
                }
            }, "" + i).start();
        }

        // 如果还有工作线程，main 线程一直等待
        while (Thread.activeCount() > 1) {
            Thread.yield();
        }
        System.out.println(count);
    }
}
