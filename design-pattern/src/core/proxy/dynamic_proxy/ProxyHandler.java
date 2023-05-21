package core.proxy.dynamic_proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/2/15
 */
public class ProxyHandler implements InvocationHandler {

    private Object target;

    public ProxyHandler(Object target) {
        this.target = target;
    }

    // 所有执行代理对象的方法都会被替换成执行 invoke 方法
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(method.getName());

        System.out.println("before method invoke");

        /**
         * 如果将 invoke 方法中的 Object result = method.invoke(realSubject, args);
         * 改为 Object result = method.invoke(proxy, args); 实际上就是
         * 将调用的对象从实际的实现类 target 改为了代理类 proxy。
         * 这样做将导致代理对象一直在调用自身的方法，陷入无限循环，
         * 最终会导致 StackOverflowError 错误
         */

        // retVal 返回值
        Object retVal = method.invoke(target, args);
        System.out.println("after method invoked");

        return null;
    }
}
