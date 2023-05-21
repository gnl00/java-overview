package test;

import java.util.Arrays;
import java.util.Scanner;

/**
 * TODO
 *
 * @author gnl
 */
public class Test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            int color = sc.nextInt();
            sc.nextLine();
            String tableStr = sc.nextLine();

            findLongest(color, tableStr);
        }
    }

    public static void findLongest(int color, String tableStr) {
        String[] arr = tableStr.split(" ");

        int len = arr.length;
        int[] dp = new int[len];
        Arrays.fill(dp, 0);

        for (int i = 0; i < len; i++) {
            String[] tmp = Arrays.copyOf(arr, arr.length);
            int current = Integer.parseInt(tmp[i]);

            // 先下子再计算长度
            // 当前位置为 0 才能下子
            if (0 == current) {
                // 下子
                tmp[i] = color + "";

                // 计算长度
                int maxCount = 0;
                int count = 0;
                for (int j = 0; j < len; j++) {
                    if (Integer.parseInt(tmp[j]) == color) {
                        count++;
                    } else {
                        // save latest count
                        maxCount = Math.max(maxCount, count);

                        count = 0;
                    }
                }
                dp[i] = maxCount;
            }
        }

        // 如果存在多个相同的最长的值，选取最靠近中间的值
        int index = 0;

        boolean hasSame = false;
        int max = 0;
        int[] sames = new int[len];
        Arrays.fill(sames, 0);
        for (int i = 0; i < len; i++) {
            if (dp[i] > max) {
                max = Math.max(dp[i], max);
                index = i;
            }
            if (max != 0 && dp[i] == max) {
                hasSame = true;
                sames[i] = 1;
            }
        }

        if (!hasSame) {
            System.out.println(index);
        } else {
            int minSpan = Integer.MAX_VALUE;
            int mid = len / 2;
            for (int i = 0; i < len; i++) {
                int span = Math.abs(i - mid);
                if (sames[i] == 1 && span < minSpan) {
                    minSpan = span;
                    index = i;
                }
            }
            System.out.println(index);
        }

    }
}
// -1
//-1 0 1 1 1 0 1 0 1 -1 1
