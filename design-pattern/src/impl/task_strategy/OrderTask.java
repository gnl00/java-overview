package impl.task_strategy;

/**
 * Subclass of Task
 *
 * @author gnl
 * @since 2023/2/13
 */
public class OrderTask extends Task {

    @Override
    boolean doTask() {
        int flag = 0;
        if (getState() != Task.CANCELED)
            try {
                System.out.println("doTask...");
                flag = (int)(Math.random() * 10);
                // do something
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        System.out.println(flag);
        if (flag > 5) setState(Task.DONE);

        return getState() == Task.DONE;
    }
}
