package hw;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * https://www.nowcoder.com/share/jump/3573155391684313152509
 *
 * @author gnl
 * @since 2023/5/17
 */
public class HJ23 {

    public static Map<Character, Integer> map = new HashMap<>();

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextLine()) { // 注意 while 处理多个 case
            String str = in.nextLine();
            removeLeast(str);
        }
    }

    // a a b c d d d

    public static void removeLeast(String str) {
        int[] arr = new int[str.length()];
        Arrays.fill(arr, -1);

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            int count = 0;
            if (map.containsKey(ch)) {
                count = map.get(ch) + 1;
            } else {
                count = 1;
            }
            map.put(ch, count);
        }

        int min = Integer.MAX_VALUE;
        String removeStr = "";
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            min = Math.min(entry.getValue(), min);
        }

        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            if (entry.getValue() == min) {
                removeStr += entry.getKey();
            }
        }

        String res = doRemove(str, removeStr);
        System.out.println(res);
    }

    public static String doRemove(String str, String removeStr) {
        String append = "";
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (removeStr.indexOf(ch) == -1) {
                append += ch;
            }
        }

        return append;
    }
}