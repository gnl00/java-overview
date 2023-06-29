package impl.task_stat_obs.task;

/**
 * OrderTask
 *
 * @author gnl
 * @since 2023/2/14
 */
public class OrderTask extends Task {

    @Override
    public boolean process() {
        if (!isCanceled() && TaskState.INIT == getState()) {
            // 通知所有观察者
            notifyAllObservers("process");

            setState(TaskState.PROCESSING);
            try {
                System.out.println("do something...");
                Thread.sleep(3000);
                done();
                return true;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        return false;
    }

}
