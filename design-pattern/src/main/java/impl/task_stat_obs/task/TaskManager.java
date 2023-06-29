package impl.task_stat_obs.task;

import java.util.HashMap;
import java.util.Map;

/**
 * TaskManager
 *
 * @author gnl
 * @since 2023/2/14
 */
public class TaskManager {
    private static final Map<Long, Task> taskPool = new HashMap<Long, Task>();

    public static void addTask(Task task) {
        taskPool.put(task.getId(), task);
    }

    public static Task getTask(Long id) {
        return taskPool.get(id);
    }

    public static void removeTask(Long id) {
        taskPool.remove(id);
    }

    public static int getTaskSize() {
        return taskPool.size();
    }

    public static void clear() {
        taskPool.clear();
    }
}
