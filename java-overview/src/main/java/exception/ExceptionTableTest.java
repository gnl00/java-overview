package exception;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/2/21
 */
public class ExceptionTableTest {
    public static void main(String[] args) throws Exception {
        tryCatch();
        tryCatchFinally();
        testThrow();
    }

    public static void testThrow() throws Exception {
    }

    public static void tryCatch() {
        try {
            Thread.sleep(100);
            System.out.println("tryCatch - try");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void tryCatchFinally() {
        try {
            Thread.sleep(100);
            System.out.println("tryCatchFinally - try");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("tryCatchFinally - finally");
        }
    }
}
