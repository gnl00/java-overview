package impl.task_templ;

/**
 * Activity
 *
 * @author gnl
 * @since 2023/2/15
 */
public abstract class Activity implements IActivity {
    private Integer id;
    private String type; // 活动类型
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    // 参与活动
    @Override
    public abstract void join(Long userId);
}
