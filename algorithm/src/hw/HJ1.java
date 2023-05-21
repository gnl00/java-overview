package hw;

import java.util.Scanner;

/**
 * 计算字符串最后一个单词的长度，单词以空格隔开，字符串长度小于 5000。（注：字符串末尾不以空格为结尾）
 *
 * @author gnl
 * @since 2023/4/13
 */
public class HJ1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        System.out.println(calculateLen(str));
    }

    public static int calculateLen(String sentence) {
        int lastLen = 0;
        for (int i = sentence.length() - 1 ; i >= 0 ; i--) {
            if (sentence.charAt(i) != ' ') {
                lastLen ++;
            } else {
                break;
            }
        }
        return lastLen;
    }
}
