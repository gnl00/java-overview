package exception;

import java.io.IOException;

/**
 * 受检查异常编译无法通过，必须通过 throws 抛出或者 try-catch 处理
 */
public class CheckedException {
    public static void main(String[] args) {
        CheckedException e = new CheckedException();
        e.test();
    }

    public void test() {
        // throw new IOException("An IOEx");
    }
}
