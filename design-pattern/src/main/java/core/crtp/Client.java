package core.crtp;

/**
 * 奇异递归模板模式（Curiously Recurring Template Pattern, CRTP）
 * 是一种基于范型编程的设计模式，它通过递归的方式实现静态多态性，可以让子类在编译期就实现对父类的某些行为进行扩展。
 *
 * 相对于普通的模板方法模式，CRTP 的优点在于：
 * 静态多态性：在编译期就确定了子类对父类的某些行为的实现，避免了动态绑定和虚函数带来的性能开销。
 * 灵活扩展：子类可以在编译期通过递归实现父类的某些行为进行扩展，可以避免模板方法模式中需要修改父类模板方法的缺点。
 * 代码复用：通过 CRTP 模式，可以让不同的子类通过继承同一个基类，实现相同的基础行为，从而避免重复的代码编写。
 *
 * @since 2023/2/15
 * @author gnl
 */
public class Client {
    public static void main(String[] args) {
        Base base = new Base();
        base.templateMethod();
    }
}
