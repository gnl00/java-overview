package impl.task_strategy;

/**
 * HotelTask
 *
 * @author gnl
 * @since 2023/2/13
 */
public class HotelTask extends Task{
    @Override
    boolean doTask() {
        if (getState() != Task.CANCELED) {
            try {
                // do something
                Thread.sleep(2000);
                setState(Task.DONE);
                return true;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
