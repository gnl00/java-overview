package hw;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * https://www.nowcoder.com/share/jump/3573155391684310314262
 *
 * @author gnl
 * @since 2023/4/15
 */
public class HJ21 {
    public static Map<Character, Integer> table = new HashMap<>();

    static {
        table.put('a', 2);
        table.put('b', 2);
        table.put('c', 2);
        table.put('d', 3);
        table.put('e', 3);
        table.put('f', 3);
        table.put('g', 4);
        table.put('h', 4);
        table.put('i', 4);
        table.put('j', 5);
        table.put('k', 5);
        table.put('l', 5);
        table.put('m', 6);
        table.put('n', 6);
        table.put('o', 6);
        table.put('p', 7);
        table.put('q', 7);
        table.put('r', 7);
        table.put('s', 7);
        table.put('t', 8);
        table.put('u', 8);
        table.put('v', 8);
        table.put('w', 9);
        table.put('x', 9);
        table.put('y', 9);
        table.put('z', 9);
    }

    public static void main(String[] args) {
//        System.out.println((char) ((('a' - '0') + 1) + '0')); // 49
//        System.out.println('b' - '0'); // 50
//        System.out.println('z' - '0'); // 74
//        System.out.println('A' - '0'); // 17
//        System.out.println('Z' - '0'); // 42

        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        String str = in.nextLine();
        simplePW(str);
    }

    public static void simplePW(String str) {
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            String append = String.valueOf(ch);

            int res = 0;
            if ((res = isAlphabet(ch)) != -1) {
                // lower case
                if (res == 0) {
                    append = table.get(ch) + "";
                }

                // upper case
                if (res == 1) {
                    // to lower case and backwards

                    if (ch == 'Z') {
                        append = String.valueOf('a');
                    } else {
                        append = String.valueOf((char) ((ch - '0' + 1) + '0'));
                    }
                    append = append.toLowerCase();

                }
            }

            result += append;
        }
        System.out.println(result);

    }

    public static int isAlphabet(char ch) {
        int n = ch - '0';

        // 小写 return 0
        if (n >= 'a' - '0' && n <= 'z' - '0') {
            return 0;
        }

        // 大写 return 1
        if (n >= 'A' - '0' && n <= 'Z' - '0') {
            return 1;
        }

        // 非字母返回 -1
        return -1;
    }
}
