package hw;

import java.util.Scanner;

/**
 * 功能:输入一个正整数，按照从小到大的顺序输出它的所有质因子（重复的也要列举）（如 180 的质因子为 2 2 3 3 5）
 *
 *
 * 数据范围：1≤n≤2×10^9+14
 *
 * @author gnl
 * @since 2023/4/14
 */
public class HJ6 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Integer num = sc.nextInt();
        getFactor(num);
    }

    public static void getFactor(int num) {
        String res = "";
        int divisor = 2;
        while (num > 1) {
            if (num % divisor == 0) {
                res += divisor + " ";
                num = num / divisor;
            } else {
                divisor++;
            }
        }

        System.out.println(res);
    }

}
