package core.observer;

/**
 * Client
 *
 * @author gnl
 * @since 2023/2/14
 */
public class Client {
    public static void main(String[] args) {
        Observer cObserver = new ConcreteObserver();
        Observer cObserver2 = new ConcreteObserver2();
        ConcreteSubject cSubject = new ConcreteSubject();

        cSubject.registerObserver(cObserver);
        cSubject.registerObserver(cObserver2);

        cSubject.doSomething();
    }
}
