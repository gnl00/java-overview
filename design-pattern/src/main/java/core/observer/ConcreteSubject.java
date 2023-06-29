package core.observer;

/**
 * ConcreteSubject
 *
 * @since 2023/2/14
 * @author gnl
 */
public class ConcreteSubject extends Subject {

    public ConcreteSubject() {
        super();
    }

    @Override
    void doSomething() {
        System.out.println("doSomething...");

        try {
            Thread.sleep(2000);
            System.out.println("done!");

            // 任务完成通知对应的 Observer
            notifyAllObservers();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
