package dp;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/5/17
 */
public class Fibonacci {
    public static void main(String[] args) {
        int res = fib_recursive(4);
        System.out.println(res);
    }

    // 1 1 2 3 5 8
    public static int fib_recursive(int n) {
        if (n == 1 || n == 2) return 1;
        return fib_recursive(n-1) + fib_recursive(n-2);
    }
}
