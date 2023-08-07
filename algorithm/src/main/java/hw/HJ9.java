package hw;

import java.util.Scanner;

/**
 * 输入一个 int 型整数，按照从右向左的阅读顺序，返回一个不含重复数字的新的整数。
 * 保证输入的整数最后一位不是 0 。
 *
 * @author gnl
 * @since 2023/4/14
 */
public class HJ9 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        printReserved(str);
    }

    public static void printReserved(String str) {
        int len = str.length() - 1;

        StringBuilder builder = new StringBuilder();
        for (int i = len; i >= 0 ; i--) {
            builder.append(builder.indexOf(str.charAt(i) + "") != -1 ? "" : str.charAt(i) + "");
        }

        System.out.println(builder.toString());
    }
}
