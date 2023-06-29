package impl.task_stat_obs.task;

import impl.task_stat_obs.observer.IObserver;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract Task class
 * 任务模型设计，需要重点关注状态的流转变更，以及状态变更后的消息通知
 *
 * @author gnl
 * @since 2023/2/14
 */
public abstract class Task {

    private Long id;
    private TaskState state;
    private Map<Integer, IObserver> observers = null;

    Task() {
        setState(TaskState.INIT);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskState getState() {
        return state;
    }

    protected void setState(TaskState state) {
        this.state = state;
    }

    public boolean registerObserver(IObserver observer) {
        if (observers == null) {
            observers = new HashMap<>();
        }

        if (observer != null) {
            observers.put(observer.hashCode(), observer);
            return true;
        }

        return false;
    }

    public boolean unregisterObserver(Integer hashCode) {
        if (observers == null || observers.size() == 0 || hashCode == null) {
            return false;
        }

        if (observers!= null) {
            return observers.remove(hashCode) != null;
        }

        return false;
    }

    public void notifyObserver(Integer hashCode) {}

    public void notifyAllObservers(String msg) {
        if (observers!= null) {
            observers.forEach((id, observer) -> observer.update(msg));
        }
    }

    // task start
    public abstract boolean process();

    public boolean pause() {
        if (!isCanceled() && (TaskState.INIT == state || TaskState.PROCESSING == state)) {
            notifyAllObservers("pause");

            setState(TaskState.PAUSED);
            return true;
        }
        return false;
    }

    public boolean cancel() {
        if (!isCanceled() && (TaskState.INIT == state || TaskState.PROCESSING == state || TaskState.PAUSED == state)) {
            notifyAllObservers("cancel");
            setState(TaskState.CANCELED);
            return true;
        }
        return false;
    }

    // the task is ended
    public boolean done() {
        if (!isCanceled() && TaskState.PROCESSING == getState()) {
            notifyAllObservers("done");

            setState(TaskState.FINISHED);
            return true;
        }
        return false;
    }

    protected boolean isCanceled() {
        return state == TaskState.CANCELED;
    }

    public void checkState() {
        System.out.println("task state: " + state.getValue());
    }
}
