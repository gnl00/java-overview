package impl.task_strategy;

/**
 * Coupon class
 *
 * @author gnl
 * @since 2023/2/13
 */
public class Coupon {

    // state of task, taskState=1 only if a corresponding task is done, else taskState=0
    private static volatile Coupon COUPON = null;

    private Coupon() {}

    private static boolean checkTaskState(int state) {
        return state == Task.DONE;
    }

    public static void getCoupon(Task task) {

        // check coupon instance
        if (COUPON == null) {
            synchronized (Coupon.class) {
                if (COUPON == null) {
                    COUPON = new Coupon();
                }
            }
        }

        if (checkTaskState(task.getState()) && task.getRedeemed() != 1) {
            int cp = (int)Math.ceil(Math.random() * 10);
            getCouponSuccess(cp);
            task.setRedeemed(1);
        } else if (1 == task.getRedeemed()) {
            redeemed();
        } else {
            getCouponFailure();
        }
    };

    private static void getCouponSuccess(int coupon) {
        System.out.println("Congress! You will get a " + coupon + " yuan coupon");
    }

    private static void getCouponFailure() {
        System.out.println("Sorry! it seems like you have not finished the task");
    }

    private static void redeemed() {
        System.out.println("Ops! the coupon has already been redeemed");
    }

}
