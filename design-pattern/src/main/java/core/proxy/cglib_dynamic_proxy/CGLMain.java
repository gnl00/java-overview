package core.proxy.cglib_dynamic_proxy;

public class CGLMain {
    public static void main(String[] args) {
        CGLTarget proxy = (CGLTarget) new CGLProxy().getInstance(new CGLTarget());
        proxy.sayHi();
    }
}
