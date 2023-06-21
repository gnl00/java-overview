package jvm;

public class ClassLoadTest {
    public static final String JVM = "JVM";
    public static void main(String[] args) throws ClassNotFoundException {
        Admin a = new Admin();
        System.out.println("hello, " + JVM);
    }
}
