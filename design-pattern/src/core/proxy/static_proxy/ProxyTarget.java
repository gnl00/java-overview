package core.proxy.static_proxy;

/**
 * 被代理类
 *
 * @author gnl
 * @since 2023/2/15
 */
public class ProxyTarget extends AbstractTarget {
    @Override
    void doSomething() {
        System.out.println("ProxyTarget doSomething");
    }
}
