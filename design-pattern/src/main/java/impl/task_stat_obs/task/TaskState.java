package impl.task_stat_obs.task;

/**
 * 描述任务的"状态"
 *
 * @since 2023/2/14
 * @author gnl
 */
public enum TaskState {
    INIT(0,"初始化"),
    PROCESSING(1,"进行中"),
    PAUSED(2,"已暂停"),
    FINISHED(3,"已完成"),
    CANCELED(-1,"已取消");

    private String value;
    private int code;

    TaskState(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }
}
