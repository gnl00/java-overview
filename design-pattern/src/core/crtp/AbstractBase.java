package core.crtp;

/**
 * 奇异递归模板模式（Curiously Recurring Template Pattern, CRTP）
 *
 * @author gnl
 * @since 2023/2/15
 */
public abstract class AbstractBase<T extends AbstractBase<T>>  {
    abstract void method();

    public void templateMethod() {
        System.out.println("before method");
        // 当前对象强制转换为泛型参数 T
        // 调用 method 方法，实现在运行时动态绑定的效果
        ((T)this).method();
        System.out.println("after method");
    }

}
