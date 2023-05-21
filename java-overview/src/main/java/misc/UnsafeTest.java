package misc;

import sun.misc.Unsafe;

/**
 * Unsafe Test
 *
 * 必须要 BootstrapClassLoader 加载的类才可以获取 Unsafe 对象
 *
 * 但是可以通过反射的方式获取：
 * Field field = Unsafe.class.getDeclaredField("theUnsafe");
 * field.setAccessible(true);
 *
 * @author gnl
 * @since 2023/2/13
 */
public class UnsafeTest {

    // 必须要 BootstrapClassLoader 加载的类才可以获取 Unsafe 对象
    private static final Unsafe U = Unsafe.getUnsafe();
    private static long count = 0;

    public static long getCountAddress(long count) {
        return U.getAddress(count);
    }

    public static void main(String[] args) {
        System.out.println(getCountAddress(count)); // 得到异常 Caused by: java.lang.SecurityException: Unsafe
    }
}
