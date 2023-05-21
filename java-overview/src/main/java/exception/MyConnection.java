package exception;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/2/21
 */
public class MyConnection implements AutoCloseable {
    public void start() throws Exception {
        throw new Exception("start exception");
    }

    @Override
    public void close() throws Exception {
        throw new Exception("close exception");
    }
}
