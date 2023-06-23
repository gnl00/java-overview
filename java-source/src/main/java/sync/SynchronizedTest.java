package sync;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/3/13
 */
public class SynchronizedTest {

    public void method1() {
        synchronized(this) {
            // do nothing
        }
    }

    public void method2() {
        synchronized(SynchronizedTest.class) {
            // do something
        }
    }
}
