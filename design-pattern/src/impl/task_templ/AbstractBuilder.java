package impl.task_templ;

/**
 * AbstractBuilder
 *
 * @author gnl
 * @since 2023/2/15
 */
public abstract class AbstractBuilder<T extends AbstractBuilder<T>> extends Activity {

    // build 方法只需要专注 build 过程
    public abstract T build(String name);

    // build 前参数/后结果检查交给 buildTemplate
    public T buildInstance(String name) {

        // before build
        System.out.println("before buildInstance");

        T obj = build(name);

        // after build
        System.out.println("after buildInstance");

        return obj;
    }

}
