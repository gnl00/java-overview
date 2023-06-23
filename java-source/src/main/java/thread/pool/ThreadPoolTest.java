package thread.pool;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/3/10
 */
public class ThreadPoolTest {
    public static void main(String[] args) throws InterruptedException {
        String initial = "hello";

        for (int i = 0; i < 500; i++) {
            StringBuilder builder = new StringBuilder();
            builder.append(initial);
        }

        while (true) {}
    }
}
