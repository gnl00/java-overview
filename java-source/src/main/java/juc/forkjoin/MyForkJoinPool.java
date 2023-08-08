package juc.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class MyForkJoinPool {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool fjp = ForkJoinPool.commonPool();

        long start = System.currentTimeMillis();

        MyForkJoinTask task = new MyForkJoinTask(1, 1000000000, 100000000);
        ForkJoinTask<Long> resp = fjp.submit(task);
        System.out.println(resp.get());

        long end = System.currentTimeMillis();
        System.out.println("cost time ==> " + (end - start));
    }
}
