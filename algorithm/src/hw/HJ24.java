package hw;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

/**
 * https://www.nowcoder.com/share/jump/3573155391684324609549
 *
 * @author gnl
 * @since 2023/5/17
 */
public class HJ24 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextLine()) { // 注意 while 处理多个 case
            int n = in.nextInt();
            in.nextLine();
            String str = in.nextLine();
            validate(str);
        }
    }

    private static void validate(String str) {
        String[] arr = str.split(" ");

        int len = arr.length;
        int[] dp = new int[len];
        Arrays.fill(dp, 0);

        // if len == 1
        if (len == 1) {
            System.out.println(0);
        }

        if (len == 2 && Objects.equals(arr[0], arr[1])) {
            System.out.println(1);
        }

        for (int i = 1; i < len - 1; i++) {
            dp[i] = dp[i - 1];

            int l = Integer.parseInt(arr[i-1]);
            int mid = Integer.parseInt(arr[i]);
            int r = Integer.parseInt(arr[i + 1]);


            // 单增
            if (l < mid && mid < r) {
                dp[i + 1] = dp[i] = dp[i - 1];
            }
            // 单减
            if (l > mid && mid > r) {
                dp[i + 1] = dp[i] = dp[i - 1];
            }
            // 中间高两边低
            if (l > mid && mid > r) {
                dp[i + 1] = dp[i] = dp[i - 1];
            }
            // 啥也不干

            // 中间低两边高
            // 最左或者最右 + 1
            if (l > mid || mid < r) {
                dp[i + 1] = dp[i] = dp[i - 1];
            }

            if (l <= mid) {
                // 如果后一个大于或者等于前一个，加 1
                dp[i] = dp[i - 1] + 1;
            }

        }

        // 处理最后一个元素
        if (Integer.parseInt(arr[len - 2]) <= Integer.parseInt(arr[len - 1])) {
            dp[len - 1] = dp[len - 1] + 1;
        }

        for (int n:dp) {
            System.out.println(n);
        }
    }
}
