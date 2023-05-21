package hw;

import java.util.*;

/**
 * 数据表记录包含表索引 index 和数值 value（int 范围的正整数），
 * 请对表索引相同的记录进行合并，即将相同索引的数值进行求和运算，输出按照 index 值升序进行输出。
 *
 * @author gnl
 * @since 2023/4/14
 */
public class HJ8 {
    private static Map<Integer, Integer> map = new HashMap<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        sc.nextLine();

        while (num > 0) {
            String str = sc.nextLine();
            mergeKV(str);

            num--;
        }

        ArrayList<Map.Entry<Integer, Integer>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, Map.Entry.comparingByKey());

        for (Map.Entry<Integer, Integer> entry : list) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    public static void mergeKV(String str) {
        String[] ss = str.split(" ");
        int k = Integer.valueOf(ss[0]);
        int v = Integer.valueOf(ss[1]);
        if (map.containsKey(k)) {
            map.put(k, map.get(k) + v);
        } else {
            map.put(k, v);
        }
    }
}
