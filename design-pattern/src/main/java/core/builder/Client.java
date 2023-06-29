package core.builder;

/**
 * Client
 *
 * @author gnl
 */
public class Client {
    public static void main(String[] args) {
        // 指定创建对象 -- 我需要什么
        ConcreteBuilder builder = new ConcreteBuilder();
        //  指定创造者 -- 需要谁指导
        HouseDirector houseDirector = new HouseDirector(builder);
        // 获得结果
        House house = houseDirector.construct();
        System.out.println(house);
    }
}
