package thread.test;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/2/4 下午12:15
 */
public class MyRunable implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " MyRunable is running");
    }
}
