package test;

// 第 1 行 1 个
// 第 2 行 2 个
// 第 3 行 3 个
// ...
// 第 n 行 n 个

// 奇数正序
// 偶数逆序

// 每个数字占 4 个位置，不足 * 占位
// 数字之间相距 4 个空格

// 1
// 3 2
// 6 5 4
// 10 7 8 9

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        print(n);
    }

    public static void print(int n) {
        if (n < 1 || n > 30) return;

        int num = 1;
        for (int i = 0; i < n; i++) {
            String append = "";
            for (int j = 0; j <= i; j++) {
                append += num++ + " ";
            }

            // 换位置
            String result = changeIndex(append);
            String[] strings = result.split(" ");
            for (int j = 0; j < strings.length; j++) {
                String s = strings[j];
                int len = s.length();
                int startNum = 0;

                if (len < 4) {
                    startNum = 4 - len;
                }

                for (int k = 0; k < startNum; k++) {
                    s += "*";
                }



                if (j == 0) {
                    // 左边空格数
                    // n 行
                    // n - 1 => 4
                    // n => 0
                    int spaceNum = (n - i - 1) * 4;
                    String space = "";
                    for (int k = 0; k < spaceNum; k++) {
                        space += " ";
                    }

                    s = space + s;
                }

                System.out.print(s + "    ");
            }
            System.out.println();
        }
    }

    // 1 奇 1 奇
    // 2 偶 2 3 奇
    // 3 偶 4 5 6 偶
    // 4 奇 7 8 9 10 偶
    // 5 奇 11 12 13 14 15 奇
    // 6 16 17 18 19 20 21

    // 奇 1
    // 奇 3 2 偶
    // 偶 6 5 4 偶
    // 7 8 9 10
    //
    // 偶 10 7 8 9 奇
    // 奇 11 14 13 12 15 奇
    // 20 17 18 19 16 21

    public static String changeIndex(String str) {
        str = str.trim();
        if (str.length() == 1) return str;

        String[] arr = str.split(" ");

        if (str.length() == 2) {
            String out = "";
            for (int i = arr.length - 1; i >= 0; i--) {
                out += arr[i] + " ";
            }
            return out;
        }


        int initLen;
        if (arr.length % 2 == 0) {
            initLen = arr.length / 2;
        } else {
            initLen = (arr.length / 2) + 1;
        }

        int[] odds = new int[initLen]; // 奇数
        int oddIndex = 0;
        int[] evens = new int[initLen]; // 偶数
        int evenIndex = 0;

        for (int i = 0; i < arr.length; i++) {
            int cur = Integer.parseInt(arr[i]);
            if (cur % 2 == 0) {
                evens[evenIndex++] = cur;
            } else {
                odds[oddIndex++] = cur;
            }
        }

        String[] outArr = new String[arr.length];

        int first = Integer.parseInt(arr[0]);
        if (first % 2 == 0) { // 如果原来以偶数开头，先逆序输出偶数
            int outIndex = 0;

            for (int i = evens.length - 1; i >= 0 ; i--) {
                if (outIndex < outArr.length) {
                    outArr[outIndex] = evens[i] + "";
                }
                outIndex += 2;
            }
            // 再顺序奇数
            int oddOutIndex = 0;
            for (int i = 0; i < outArr.length; i++) {
                if (null == outArr[i] && oddOutIndex < odds.length) {
                    outArr[i] = odds[oddOutIndex++] + "";
                }
            }

        } else { // 如果原来以奇数开头
            int outIndex = 0;
            // 先顺序输出奇数
            for (int i = 0; i < odds.length; i++) {

                if (outIndex < outArr.length) {
                    outArr[outIndex] = odds[i] + "";
                }
                outIndex += 2;
            }

            // 再逆序偶数
            int evenOutIndex = evens.length - 1;
            for (int i = 0; i < outArr.length; i++) {
                if (null == outArr[i] && evenOutIndex >= 0) {
                    outArr[i] = evens[evenOutIndex--] + "";
                }
            }
        }

        String append = "";
        for (String s : outArr) {
            append += s + " ";
        }
        return append.trim();
    }
}
