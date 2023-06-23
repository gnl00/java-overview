package thread.print;

/**
 * 利用 join 方法实现 3个线程交替打印 ABC
 *
 * @author gnl
 * @since 2023/2/4
 */
public class JoinPrint {
    public static void print(Thread preThread) {
        if (preThread != null) {
            try {
                preThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(Thread.currentThread().getName());
    }
    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 10; i++) {
            Thread a = new Thread(() -> {
                print(null);
            }, "a");

            Thread b = new Thread(() -> {
                print(a);
            }, "b");

            Thread c = new Thread(() -> {
                print(b);
            }, "c");

            // 每个线程的 start 方法只能执行一次，要重新创建线程，才能再次执行 start
            a.start();
            b.start();
            c.start();

            Thread.sleep(10);
        }

    }
}
