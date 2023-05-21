package thread.test;

/**
 * ThreadLocalTest
 *
 * @author gnl
 * @since 2023/2/15
 */
public class ThreadLocalTest {
    public static void main(String[] args) {

        // 默认无初始值
        ThreadLocal<String> tl = new ThreadLocal<>();

        // 设置默认值
        ThreadLocal<String> withInitialThreadLocal = ThreadLocal.withInitial(() -> {
            System.out.println("setting initial");
            return "initialValue";
        });

        new Thread(() -> {
            withInitialThreadLocal.get();
        }, "AAA").start();


//        new Thread(() -> {
//            System.out.println(Thread.currentThread().getName());
//
//            System.out.println(withInitialThreadLocal.get());
//
//            tl.set("hello");
//            System.out.println(tl.get());
//
//        }, "A").start();
//
//        new Thread(() -> {
//            System.out.println(Thread.currentThread().getName());
//
//            System.out.println(withInitialThreadLocal.get());
//
//            tl.set("world");
//            System.out.println(tl.get());
//        }, "B").start();
    }
}
