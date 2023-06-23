package thread.print;

import java.util.concurrent.Semaphore;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/2/6 下午12:13
 */
public class SemaphorePrint2 {
    // 需要先执行获得 s1 信号量的线程，因此初始值设置为 1
    private static final Semaphore s1 = new Semaphore(1);
    private static final Semaphore s2 = new Semaphore(0);
    private static final Semaphore s3 = new Semaphore(0);

    private static void print(Semaphore current, Semaphore next) {
        for (int i = 0; i < 10; i++) {
            try {
                current.acquire();
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                next.release();
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            print(s1, s2);
        }, "a").start();
        Thread.sleep(10); // 保证执行顺序
        new Thread(() -> {
            print(s2, s3);
        }, "b").start();
        Thread.sleep(10);
        new Thread(() -> {
            print(s3, s1);
        }, "c").start();
    }
}
