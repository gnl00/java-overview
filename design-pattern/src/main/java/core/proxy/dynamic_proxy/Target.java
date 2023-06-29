package core.proxy.dynamic_proxy;

/**
 * HelloImpl
 *
 * @author gnl
 * @since 2023/2/15
 */
public class Target implements ITarget {
    @Override
    public void sayHello(String name) {
        System.out.println("hello  " + name);
    }
}
