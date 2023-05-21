package impl.task_templ;

/**
 * 在任务型活动的参与方法上增加一层风险控制
 *
 * @author gnl
 * @since 2023/2/15
 */
public class Client {
    public static void main(String[] args) {
        Activity activity = new ConcreteBuilder().buildInstance("aaa");
        System.out.println(activity);
        activity.join(1L);

        Activity orderActivity = new OrderActivityBuilder().buildInstance("bbb");
        System.out.println(orderActivity);
    }
}
