package juc.forkjoin;

import java.util.concurrent.atomic.AtomicLong;

// 计算 1 加到 1000000000
public class NoForkJoinPool {
    public static void main(String[] args) {
        FJTemplate fj = new FJTemplate();
        NoForkJoinPool proxy = (NoForkJoinPool) fj.getInstance(new NoForkJoinPool());
        proxy.noThread();
        proxy.withThread();
    }

    public void noThread() {
        long sum = 0;
        for (long i = 1; i <= 1000000000; i++) {
            sum += i;
        }
        System.out.println(sum);
    }

    public void withThread() {
        AtomicLong globalSum = new AtomicLong();

        long from = 0;
        long max = 1000000000;
        long span = max/10;

        // 10 threads
        for (int i = 1; i <= 10; i++) {
            long finalFrom = from; // 0          1000000001
            long to = span * i; // 1000000000 2000000000
            new Thread(() -> {
                long sum = 0;
                for (long j = finalFrom; j <= to; j++) {
                    sum += j;
                }
                System.out.println("sum now: " + sum);
                long gs = globalSum.addAndGet(sum);
                System.out.println("global sum now: " + gs);
            }).start();
            from = to + 1;
        }
    }
}
