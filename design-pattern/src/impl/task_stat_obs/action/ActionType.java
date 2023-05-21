package impl.task_stat_obs.action;

/**
 * 描述客户端的"动作"
 *
 * @author gnl
 * @since 2023/2/14
 */
public enum ActionType {
    INIT(0,"初始化/新建"),
    START(1,"开始"),
    PAUSE(2,"暂停"),
    CANCELED(-1,"取消");

    private int code;
    private String value;

    ActionType(int code, String value) {
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
