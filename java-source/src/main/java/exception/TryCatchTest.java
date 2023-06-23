package exception;

public class TryCatchTest {
    public static void main(String[] args) {
        try {
            test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void test() throws Exception {

        // 异常屏蔽
        // 普通的 try-finally 块面对多个异常时可能会造成异常屏蔽
        MyConnection mc = new MyConnection();
        try {
            mc.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mc.close();
        }
    }
}
