package impl.task_stat_obs;

import impl.task_stat_obs.action.Action;
import impl.task_stat_obs.observer.IObserver;
import impl.task_stat_obs.observer.OrderObserver;
import impl.task_stat_obs.task.OrderTask;
import impl.task_stat_obs.task.Task;

/**
 * Client
 *
 * @author gnl
 * @since 2023/2/14
 */
public class Client {
    public static void main(String[] args) {

        IObserver observer = new OrderObserver();

        Task orderTask = new OrderTask();
        orderTask.registerObserver(observer);

        Action action = new Action(orderTask);

        action.start();
        action.pause();

    }

//    public static void process() {
//        System.out.println("processing...");
//
//        boolean retVal = task(() -> {
//            System.out.println("do something...");
//
//            int flag = (int) (Math.random() * 10);
//            try {
//                if (flag > 5) {
//                    Thread.sleep(2000);
//                    System.out.println("task done!");
//                    return true;
//                }
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            System.out.println("task failed!");
//            return false;
//        });
//
//        if (retVal) { // 任务完成
//            // setTaskStatus
//        } else { // 任务失败
//        }
//    }
//
//    public static boolean task(ITask task) {
//        return task.doProcess();
//    }
}
