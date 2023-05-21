package core.builder;

/*
 * 抽象建造者
 **/
public abstract class Builder {
    // 建房子
    protected House house = new House();
    // 屋顶
    public abstract void buildRoof();
    // 墙
    public abstract void buildWall();
    // 地基
    public abstract void buildFoundation();

    public House buildHouse() {
        return this.house;
    }
}