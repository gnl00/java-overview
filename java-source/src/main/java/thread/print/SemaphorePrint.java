package thread.print;

import java.util.concurrent.Semaphore;

/**
 * 使用 Semaphore 来控制交替打印
 *
 * @author gnl
 * @since 2023/2/6 上午11:57
 */
public class SemaphorePrint {
    private static int num = 0;

    private static final Semaphore semaphore = new Semaphore(1, true);

    // 任意时刻仅允许最多 3 个线程获取许可
    // 如果超过了许可数量,其他线程则在 acquire 中等待信号量释放
    final Semaphore semaphore3 = new Semaphore(3);

    public static void print(int target) {
        for (int i = 0; i < 10;) {
            try {
                semaphore.acquire();
                while (num % 3 == target) {
                    System.out.println(Thread.currentThread().getName());
                    num++;
                    i++;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                semaphore.release();
            }
        }
    }
    public static void main(String[] args) {
        new Thread(() -> {
            print(0);
        }, "a").start();
        new Thread(() -> {
            print(1);
        }, "b").start();
        new Thread(() -> {
            print(2);
        }, "c").start();
    }
}
