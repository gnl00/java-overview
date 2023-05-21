package hw;

import java.util.Scanner;

/**
 * 将一个英文语句以单词为单位逆序排放。例如 “I am a boy”，逆序排放后为 “boy a am I”
 * 所有单词之间用一个空格隔开，语句中除了英文字母外，不再包含其他字符
 *
 * @author gnl
 * @since 2023/4/14
 */
public class HJ13 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        printReversed(str);
    }

    public static void printReversed(String str) {
        String[] arr = str.split(" ");
        for (int i = arr.length - 1; i >= 0 ; i--) {
            String res = arr[i];
            if (i != 0) {
                res += " ";
            }
            System.out.print(res);
        }
    }
}
