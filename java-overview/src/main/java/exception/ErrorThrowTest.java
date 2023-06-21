package exception;

import java.io.IOError;

public class ErrorThrowTest {

    public void test() {
        throw new IOError(null);
    }

    public static void main(String[] args) {
        ErrorThrowTest errorTest = new ErrorThrowTest();
        try {
            errorTest.test();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("do something");
    }
}
