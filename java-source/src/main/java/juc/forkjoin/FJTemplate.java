package juc.forkjoin;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class FJTemplate implements MethodInterceptor {

    private Object target;

    public Object getInstance(Object obj) {
        this.target = obj;

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(obj.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        String name = method.getName();
        System.out.println("executing ===> " + name);

        long start = System.currentTimeMillis();

        Object resp = methodProxy.invokeSuper(o, args);

        long end = System.currentTimeMillis();

        System.out.println("total time spent ===> " + (end - start));

        return resp;
    }
}
