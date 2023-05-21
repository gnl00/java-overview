package oop;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/2/20
 */
public class TestImpl implements ITest, ITest2 {
    public static void main(String[] args) {
        // ITest.count = 2; // Cannot assign a value to final variable count

        System.out.println(count);

    }

    // TestImpl inherits unrelated defaults for test() from types ITest and ITest2
    @Override
    public void test() {
        ITest.super.test();
    }

    public void over() {}

    // public int over() { return 1; } // 'over()' is already defined

    public void over(int a) {}

    public void over(int a, String b) {}

    public void over(String a, int b) {}

}
