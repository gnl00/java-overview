package thread.print;

// 线程 a 和 b 交替打印 1、2、3
public class ThreadANBPrint123WaitNotify {
    public static void main(String[] args) {
        Thread123 a = new Thread123();
        Thread123 b = new Thread123();
        a.start();
        b.start();
    }
}

class Thread123 extends Thread {
    private static final Object MONITOR = new Object();
    @Override
    public void run() {
        while (true) {
            synchronized (MONITOR) {
                print();
                MONITOR.notifyAll();
                System.out.println(currentThread().getName() + " notifyAll");
                try {
                    System.out.println(currentThread().getName() + " waiting");
                    MONITOR.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void print() {
        System.out.println(currentThread().getName() + " printing");
        for (int i = 1; i <=3; i++) {
            System.out.println(i);
        }
    }
}
