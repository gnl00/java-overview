package impl.task_strategy;

/**
 * Abstract Task class
 *
 * @author gnl
 * @since 2023/2/13
 */
public abstract class Task {

    // status of the task 1=done or else 0
    private int state;

    // 是否已兑换
    private int redeemed;

    static final int DONE = 1;
    static final int CANCELED = -1;

    public int getRedeemed() {
        return redeemed;
    }

    public void setRedeemed(int redeemed) {
        this.redeemed = redeemed;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    abstract boolean doTask();
}
