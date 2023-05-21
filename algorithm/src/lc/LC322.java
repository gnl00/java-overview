package lc;

import java.util.Arrays;

/**
 * https://leetcode.cn/problems/coin-change/
 *
 * @author gnl
 * @since 2023/5/17
 */
public class LC322 {
    public static void main(String[] args) {
        LC322 lc322 = new LC322();

        // 2 4 5
        // 1 3 1
        int[] coins = {2, 4, 5};
        int amount = 11;
        int res = lc322.coinChange(coins, amount);
        System.out.println(res);
    }

    public int ccDownToTop(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);

        // base case
        dp[0] = 0;

        for (int i = 0; i < dp.length; i++) {
            for (int coin : coins) {
                if (i - coin < 0) continue;

                dp[i] = Math.min(dp[i], 1 + dp[i -coin]);
            }
        }

        return dp[amount] == amount + 1 ? -1 : dp[amount];
    }

    int[] memo;

    public int coinChange(int[] coins, int amount) {
        memo = new int[amount + 1];
        Arrays.fill(memo, Integer.MAX_VALUE);

        return dp(coins, amount);
    }

    public int dp(int[] coins, int amount) {
        // base case
        if (amount < 0) return -1;
        if (amount == 0) return 0;

        // find from history
        if (memo[amount] != Integer.MAX_VALUE) return memo[amount];

        int count = Integer.MAX_VALUE;
        for (int coin : coins) {
            int subProblem = dp(coins, amount - coin);
            if (subProblem == -1) continue;
            count = Math.min(count, subProblem + 1);
        }

        // save history
        memo[amount] = count == Integer.MAX_VALUE ? -1 : count;

        return count == Integer.MAX_VALUE ? -1 : count;
    }
}
