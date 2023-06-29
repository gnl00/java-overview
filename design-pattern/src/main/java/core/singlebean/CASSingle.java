package core.singlebean;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 使用 CAS 实现单例模式
 */
public class CASSingle {
    private static final AtomicReference<CASSingle> AR = new AtomicReference<>();
    private CASSingle() {}
    public static CASSingle getInstance() {
        while (true) {
            CASSingle singleton = AR.get();
            if (null != singleton) return singleton;
            singleton = new CASSingle();
            if (AR.compareAndSet(null, singleton)) return singleton;
        }
    }

    public static void main(String[] args) {
        CASSingle c1 = CASSingle.getInstance();
        CASSingle c2 = CASSingle.getInstance();
        CASSingle c3 = CASSingle.getInstance();
        System.out.println(c1);
        System.out.println(c2);
        System.out.println(c3);
    }
}
