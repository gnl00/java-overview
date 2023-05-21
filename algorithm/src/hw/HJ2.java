package hw;

import java.util.Scanner;

/**
 * 写出一个程序，接受一个由字母、数字和空格组成的字符串，和一个字符，然后输出输入字符串中该字符的出现次数。（不区分大小写字母）
 *
 * @author gnl
 * @since 2023/4/13
 */
public class HJ2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        String cChar = sc.nextLine();
        System.out.println(calcuTime(str, cChar));
    }

    public static int calcuTime(String str, String sChar) {
        StringBuilder builder = new StringBuilder();
        int[] count = new int[str.length()];

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            String s = "";
            if (isNumber(ch)) {
                s += ch + "";
            } else {
                s = (str.charAt(i) + "").toLowerCase();
            }

            int index;
            if ((index = builder.indexOf(s)) == -1) {
                builder.append(s);
                count[builder.length() - 1]++;
            } else {
                count[index]++;
            }
        }

        int cIndex = builder.indexOf(sChar.toLowerCase());

        return cIndex == -1 ? 0 : count[cIndex];
    }

    public static boolean isNumber(char ch) {
        return ch <= 9 + '0' && ch >= 0 + '0';
    }
}
