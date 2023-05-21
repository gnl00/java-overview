package core.proxy.static_proxy;

/**
 * 静态代理在使用时，需要定义接口或者父类，被代理对象（即目标对象）与代理对象一起
 * 实现相同的接口或是继承相同的父类，再由代理对象调用目标对象的对应方法
 *
 * @author gnl
 * @since 2023/2/15
 */
public class Client {
    public static void main(String[] args) {
        AbstractTarget target = new ProxyTarget();
        ProxyFactory staticFactory = new ProxyFactory(target);
        staticFactory.doSomething();
    }
}
