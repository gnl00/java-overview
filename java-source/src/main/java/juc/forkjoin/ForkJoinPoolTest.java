package juc.forkjoin;


// 计算 1 加到 10 亿
public class ForkJoinPoolTest {
    public static void main(String[] args) {
        ForkJoinPoolTest m = new ForkJoinPoolTest();
        m.noThread();

    }

    public void calcTime() {
        long start = System.currentTimeMillis();



        long end = System.currentTimeMillis();
        System.out.println("total: " + (end - start));
    }

    public void noThread() {
        long sum = 0;
        for (long i = 0; i < 1000000000; i++) {
            sum += i;
        }
        System.out.println(sum);
    }
}
