package impl.task_templ;

/**
 * OrderActivityBuilder
 *
 * @author gnl
 * @since 2023/2/15
 */
public class OrderActivityBuilder extends AbstractBuilder<OrderActivityBuilder> {

    @Override
    public OrderActivityBuilder build(String name) {

        setId(hashCode());
        setName(name);
        setType("OrderActivity");

        return this;
    }

    @Override
    public void join(Long userId) {}

}
