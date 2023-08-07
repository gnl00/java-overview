package hw;

import java.util.Scanner;

/**
 * 输入一个整数，将这个整数以字符串的形式逆序输出
 * 程序不考虑负数的情况，若数字含有 0，则逆序形式也含有 0，如输入为 100，则输出为 001
 *
 * @author gnl
 * @since 2023/4/14
 */
public class HJ11 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        printReversed(str);
    }

    public static void printReversed(String str) {
        for (int i = str.length() - 1; i >=0 ; i--) {
            System.out.print(str.charAt(i));
        }
    }
}
