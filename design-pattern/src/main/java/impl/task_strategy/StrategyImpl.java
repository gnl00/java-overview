package impl.task_strategy;

/**
 * 做一个需要用户参与的营销活动，用户完成一系列任务，最后可以得到一些奖励作为回报。
 * 活动的奖励包含外卖、酒旅 和美食等多种品类券，设计一套奖励发放方案
 *
 * 可以使用策略模式来分配不同的任务和奖品
 *
 * @author gnl
 * @since 2023/2/13
 */
public class StrategyImpl {
    public static void main(String[] args) {
        // 创建任务
        Task orderTask = new OrderTask();
        // 完成任务
        orderTask.doTask();

        Task hotelTask = new HotelTask();
        hotelTask.doTask();
        Task foodTask = new FoodTask();
        foodTask.doTask();

        // 凭任务领取奖品
        Coupon.getCoupon(orderTask);
        Coupon.getCoupon(orderTask);
        Coupon.getCoupon(hotelTask);
        Coupon.getCoupon(foodTask);

    }
}
