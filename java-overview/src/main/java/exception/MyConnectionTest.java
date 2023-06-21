package exception;

public class MyConnectionTest {
    public static void main(String[] args) {
        try (
            MyConnection mc = new MyConnection();
        ) {
            mc.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
