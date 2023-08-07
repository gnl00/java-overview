package lc;

/**
 * https://leetcode.cn/problems/minimum-path-sum/
 *
 * 给定一个包含非负整数的 m x n 网格 grid ，请找出一条从左上角到右下角的路径，使得路径上的数字总和为最小。
 * 说明：每次只能向下或者向右移动一步。
 *
 * @author gnl
 * @since 2023/5/17
 */
public class LC64 {
    public static void main(String[] args) {
        LC64 lc64 = new LC64();

        int[][] grid = {
//                {1, 3, 1},
//                {1, 5, 1},
//                {4, 2, 1}
                {1, 2, 3},
                {4, 5, 6}
        };

        int res = lc64.minPathSum(grid);
        System.out.println(res);
    }

    public int minPathSum(int[][] grid) {
        int row = grid.length;
        int col = grid[0].length;
        int[][] dp = new int[row][col];

        // base case
        dp[0][0] = grid[0][0];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {

                if (i == 0 && j > 0) { // 第一行
                    dp[i][j] = grid[i][j] + dp[i][j-1];
                } else if (j == 0 && i > 0) { // 第一列
                    dp[i][j] = grid[i][j] + dp[i-1][j];
                } else if (i > 0 || j > 0){ // 非第一行或者第一列，取上面或者左边的最小值
                    int top = dp[i-1][j];
                    int left = dp[i][j-1];
                    dp[i][j] = grid[i][j] + Math.min(top, left);
                }
            }
        }

        return dp[row-1][col-1];
    }
}
