package impl.task_stat_obs.observer;

/**
 * OrderObserver
 *
 * @author gnl
 * @since 2023/2/14
 */
public class OrderObserver implements IObserver{
    @Override
    public void update(String msg) {
        System.out.println("OrderObserver gets message: " + msg);
    }
}
