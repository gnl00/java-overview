package hw;

import java.util.*;
import java.util.stream.Collectors;

/**
 * https://www.nowcoder.com/share/jump/3573155391684376218160
 *
 * @author gnl
 * @since 2023/5/18
 */
public class HJ26 {
    public static Map<Integer, Character> map = new HashMap<>();

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextLine()) { // 注意 while 处理多个 case
            String str = in.nextLine();
            charactersSort(str);
        }
    }

    public static void charactersSort(String str) {
        int len = str.length();

        if (len == 1) {
            System.out.println(str);
            return;
        }

        int[] cases = new int[len];
        Arrays.fill(cases, 0);

        char[] chars = new char[len];
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (isCharacter(ch)) {
                if (isUpperCase(ch)) {
                    cases[i] = 1;
                }
                // to lower case
                ch = String.valueOf(ch).toLowerCase().toCharArray()[0];
                chars[i] = ch;
            } else {
                map.put(i, ch);
            }
        }

        Arrays.sort(chars);
        for (char ch : chars) {
            System.out.println(ch);
        }
    }

    public static boolean isCharacter(char ch) {
        return (ch - '0' >= 'a' - '0' && ch - '0' <= 'z' - '0') || (ch - '0' >= 'A' - '0' && ch - '0' <= 'Z' - '0');
    }

    public static boolean isUpperCase(char ch) {
        return (ch - '0' >= 'A' - '0' && ch - '0' <= 'Z' - '0');
    }
}
