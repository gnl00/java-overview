package thread.test;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/2/4 下午12:04
 */
public class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " is running");
    }
}
