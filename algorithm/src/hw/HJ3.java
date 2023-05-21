package hw;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 明明生成了N个1到500之间的随机整数。请你删去其中重复的数字，即相同的数字只保留一个，
 * 把其余相同的数去掉，然后再把这些数从小到大排序，按照排好的顺序输出。
 *
 * 数据范围：1 ≤ n ≤ 1000
 * 输入的数字大小满足 1≤n≤500
 *
 * @author gnl
 * @since 2023/4/14
 */
public class HJ3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        int[] nums = new int[num];
        for (int i = 0; i < num; i++) {
            int n = sc.nextInt();
            if (n >= 1 && n <= 500) {
                nums[i] = n;
            }
        }
        mergeAndSort(nums);
    }
    public static void mergeAndSort(int[] nums) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            int n = nums[i];
            if (!list.contains(n)) {
                list.add(n);
            }
        }
        list.sort(Integer::compare);
        for (Integer integer : list) {
            System.out.println(integer);
        }
    }
}
