package core.proxy.cglib_dynamic_proxy;

public class CGLTarget {
    public void sayHi() {
        System.out.println("core.proxy.cglib_dynamic_proxy.CGLTarget.sayHi");
        System.out.println(getClass());
    }
}
