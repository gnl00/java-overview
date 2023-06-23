package exception;

/**
 * 未受检查异常可以通用编译，在运行中如果遇到会抛出
 * 如果要处理同样需要通过 throws 抛出或者 try-catch 处理
 */
public class UnCheckedException {
    public static void main(String[] args) {
        UnCheckedException e = new UnCheckedException();
        e.test();
    }

    public void test() {
        throw new ArithmeticException("An ArithmeticException");
    }
}
