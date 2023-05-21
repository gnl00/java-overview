package core.proxy.static_proxy;

/**
 * 代理工厂，与被代理类继承/实现相同的父类或接口
 *
 * @author gnl
 * @since 2023/2/15
 */
public class ProxyFactory extends AbstractTarget {
    private AbstractTarget target;

    public ProxyFactory(AbstractTarget target) {
        this.target = target;
    }

    @Override
    void doSomething() {
        System.out.println("StaticFactory before doSomething");
        target.doSomething();
        System.out.println("StaticFactory after doSomething");
    }
}
