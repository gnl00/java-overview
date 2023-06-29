package core.builder;

/**
 * 指挥者
 *
 * @author gnl
 * @date 2023/1/29 下午12:26
 */
public class HouseDirector {

    private Builder builder;

    public HouseDirector(Builder builder) {
        this.builder = builder;
    }

    public void setBuilder(Builder builder) {
        this.builder = builder;
    }

    // 创建房子
    public House construct() {
        builder.buildFoundation();
        builder.buildWall();
        builder.buildRoof();
        return builder.house;
    }

}
