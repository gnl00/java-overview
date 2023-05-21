package hw;

import java.util.Scanner;

/**
 * 接受一个只包含小写字母的字符串，然后输出该字符串反转后的字符串。（字符串长度不超过 1000）
 *
 * @author gnl
 * @since 2023/4/14
 */
public class HJ12 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        printReversed(str);
    }

    public static void printReversed(String str) {
        for (int i = str.length() - 1; i >= 0 ; i--) {
            char ch = str.charAt(i);
            if (isLowerCase(ch)) {
                System.out.print(ch + "");
            }
        }
    }

    public static boolean isLowerCase(char c) {
        return c - '0' >= 'a' - '0' && c - '0' <= 'z' - '0';
    }
}
