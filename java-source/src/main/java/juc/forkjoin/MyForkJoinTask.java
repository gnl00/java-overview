package juc.forkjoin;


import java.util.concurrent.RecursiveTask;

public class MyForkJoinTask extends RecursiveTask<Long> {

    private long from;
    private long to;
    private long maxRange;

    public MyForkJoinTask(long from, long to, long maxRange) {
        this.from = from;
        this.to = to;
        this.maxRange = maxRange;
    }

    @Override
    protected Long compute() {

        long range = to - from;
        long sum = 0;
        if (range >= maxRange) {
            long mid = (from + to) / 2;
            MyForkJoinTask left = new MyForkJoinTask(from, mid, maxRange);
            MyForkJoinTask right = new MyForkJoinTask(mid + 1, to, maxRange);

            // fork() asynchronously, execute this task in the pool the current task is running in
            left.fork();
            right.fork();

            // join() Returns the result of the computation when it is done
            sum += left.join();
            sum += right.join();
        } else {
            for (; from <= to ; from++) {
                sum += from;
            }
        }
        return sum;
    }
}
