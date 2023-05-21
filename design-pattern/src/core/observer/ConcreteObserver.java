package core.observer;

/**
 * ConcreteObserver
 *
 * @since 2023/2/14
 * @author gnl
 */
public class ConcreteObserver extends Observer {

    @Override
    void update() {
        // get update notification
        System.out.println("ConcreteObserver update notification");
    }
}
