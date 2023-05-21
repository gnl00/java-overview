package impl.task_stat_obs.action;

import impl.task_stat_obs.task.Task;
import impl.task_stat_obs.task.TaskState;

/**
 * default action
 * Action 类是和客户端直接交互的类
 *
 * @author gnl
 * @since 2023/2/14
 */
public class Action {
    private ActionType type;
    private Task task;

    public Action(Task task) {
        type = ActionType.INIT;
        this.task = task;
    }

    public boolean start() {
        System.out.println("action start");

        if (!isCanceled()) {
            type = ActionType.START;
            task.process();
            return true;
        }
        return false;
    }

    public boolean pause() {
        System.out.println("action pause");

        if (!isCanceled() && task.getState() == TaskState.PROCESSING) {

            type = ActionType.PAUSE;
            task.pause();

            return true;
        } else {
            System.out.println("pause failed");
        }
        return false;
    }

    public boolean cancel() {
        System.out.println("action cancel");

        if (!isCanceled() && type != ActionType.CANCELED) {

            type = ActionType.PAUSE;
            task.pause();

            return true;
        } else {
            System.out.println("cancel failed");
        }
        return false;
    }

    public boolean isCanceled() {
        return task.getState() == TaskState.CANCELED;
    }
}
