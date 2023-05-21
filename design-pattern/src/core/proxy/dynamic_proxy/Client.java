package core.proxy.dynamic_proxy;

import java.lang.reflect.Proxy;

/**
 * 代理类在程序运行时创建的代理方式被成为动态代理
 *
 * @author gnl
 * @since 2023/2/15
 */
public class Client {
    public static void main(String[] args) {
        /**
         * new Target() 是在创建代理对象时使用的
         * 实际上，在使用动态代理时，目标类的实例不是在代理类中直接实例化的，
         * 而是在代理对象被调用时，由 JVM 在运行时动态生成。
         * 代理类实现了被代理类实现的接口，并将被代理类实例化为目标对象。
         * 动态代理本质上是在运行时创建了一个实现被代理接口的匿名类，并在该类中调用了被代理对象的方法。
         */
        ProxyHandler handler = new ProxyHandler(new Target());
        ITarget hello = (ITarget) Proxy.newProxyInstance(ITarget.class.getClassLoader(), new Class[]{ ITarget.class }, handler);
        hello.sayHello("AAA");
    }
}
