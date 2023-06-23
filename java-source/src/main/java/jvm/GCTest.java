package jvm;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/3/16
 */
public class GCTest {
    public static void main(String[] args) {
        new Thread(() -> {
            byte[] arr1 = new byte[4 * 1024 * 1024];
            arr1 = new byte[8 * 1024 * 1024];
            arr1 = new byte[10 * 1024 * 1024];

            byte[] arr2 = new byte[4 * 1024 * 1024];
            arr2 = null;

            byte[] arr3 = new byte[4 * 1024 * 1024];
            byte[] arr4 = new byte[4 * 1024 * 1024];
            byte[] arr5 = new byte[4 * 1024 * 1024];
            byte[] arr6 = new byte[4 * 1024 * 1024];

            // 手动进行 FGC
            System.gc();

        }).start();

        new Thread(() -> {
            while (1 == 1) {
                GCTest o = new GCTest();
            }
        }).start();

    }
}
