package thread.print;

/**
 * 2 个线程交替打印 1-100
 *
 * @author gnl
 * @since 2023/2/4
 */
public class WaitNotifyPrint100 {
    private static int num = 1;
    private final static Object MONITOR = new Object();

    public static void print() {
        synchronized (MONITOR) {
            while (num <= 100) {
                System.out.println(num++ + " ==> " + Thread.currentThread().getName());
                MONITOR.notify();
                try {
                    MONITOR.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            // 避免最后一个线程 wait 之后没有别的线程唤醒
            MONITOR.notify();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Thread a = new Thread(() -> {
            print();
        });
        a.setName("a");

        Thread b = new Thread(() -> {
            print();
        });
        b.setName("b");

        a.start();

        // 正常来说，线程a 和线程b 根据 CUP 的轮换执行
        // join 方法加入后，线程a 执行完毕再执行 线程b
        // a.join();

        b.start();

        a.join();
        b.join();
    }
}
