package dp;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/3/5
 */
public class BackpackProblem {
    public static void main(String[] args) {
        int[][] a = {
                {2, 1, 3},
                {4, 2, 3},
        };

        int[] w = {2, 1, 3};
        int[] v = {4, 2, 3};

        backpack(3, 4, w, v);
    }

    public static void backpack(int n, int w, int[] wArr, int[] vArr) {
        int[][] dp = new int[n][];


    }
}
