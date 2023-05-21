package core.builder;

/**
 * 具体建造者
 *
 * @author gnl
 * @date 2023/1/29 下午12:22
 */
public class ConcreteBuilder extends Builder {
    @Override
    public void buildRoof() {
        house.roof();
    }

    @Override
    public void buildWall() {
        house.wall();
    }

    @Override
    public void buildFoundation() {
        house.foundation();
    }
}
