package hw;

import java.util.Scanner;

/**
 * 输入一个 int 型的正整数，计算出该 int 型数据在内存中存储时 1 的个数。
 *
 * @author gnl
 * @since 2023/4/14
 */
public class HJ15 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String binaryStr = Integer.toBinaryString(n);
        int count = 0;

        for (int i = 0; i < binaryStr.length(); i++) {
            if (binaryStr.charAt(i) == '1') {
                count++;
            }
        }
        System.out.println(count);
    }
}
