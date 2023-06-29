package core.proxy.cglib_dynamic_proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CGLProxy implements MethodInterceptor {

    private Object target;

    public Object getInstance(Object target) {
        this.target = target;

        // 使用 Enhancer 创建代理类
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        // 被代理类上所有的方法调用都会调用 Callback 对象中的 intercept 方法
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("[before] " + method.getName());

        // invoke 会直接调用被代理类的方法，陷入递归调用。会造成 StackOverflowError
        // invokeSuper 调用代理类 XXX$$EnhancerByCGLIB$$51fa4048
        Object result = methodProxy.invokeSuper(o, args);

        System.out.println("[after] " + method.getName());

        return result;
    }
}
