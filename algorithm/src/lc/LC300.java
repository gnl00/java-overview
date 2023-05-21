package lc;

import java.util.Arrays;

/**
 * https://leetcode.cn/problems/longest-increasing-subsequence/
 *
 * @author gnl
 * @since 2023/5/17
 */
public class LC300 {
    public static void main(String[] args) {
        int[] nums = {10, 9, 2, 5, 3, 7, 101, 18};
        int maxLen = lengthOfLIS(nums);
        System.out.println(maxLen);
    }

    /*
    * len =
    *
    * */

    public static int lengthOfLIS(int[] nums) {
        int[] dp = new int[nums.length];
        Arrays.fill(dp, 1);

        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }

        int max = -1;
        for (int count : dp) {
            max = Math.max(max, count);
        }

        return max;
    }
}
