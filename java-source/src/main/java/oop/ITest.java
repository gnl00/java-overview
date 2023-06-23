package oop;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/2/20
 */
public interface ITest {
    int count = 1;

    default void test() {
        System.out.println("ITest");
    }
}
