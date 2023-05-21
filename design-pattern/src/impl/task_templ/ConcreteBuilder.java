package impl.task_templ;

/**
 * ConcreteBuilder
 *
 * @author gnl
 * @since 2023/2/15
 */
public class ConcreteBuilder extends AbstractBuilder<ConcreteBuilder> implements RiskControl {
    @Override
    public ConcreteBuilder build(String name) {

        riskCheck();

        setId(hashCode());
        setName(name);
        setType("concrete");

        return this;
    }

    @Override
    public boolean riskCheck() {
        System.out.println("ConcreteBuilder riskCheck");
        return true;
    }

    @Override
    public void join(Long userId) {
        if (userId != null) {
            System.out.println("user join success userId: " + userId);
        }
    }
}
