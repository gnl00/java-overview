package hw;

import java.util.Scanner;

/**
 * •输入一个字符串，请按长度为8拆分每个输入字符串并进行输出；
 * •长度不是 8 整数倍的字符串请在后面补数字0，空字符串不处理。
 * (每个字符串长度小于等于100)
 *
 * 思路：放入二维数组中
 * 数组行数 = 字符串长度 / 8（需要处理整除和非整除）
 * 数组列数 = 8
 *
 * @author gnl
 * @since 2023/4/14
 */
public class HJ4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        splitPer8(str);
    }

    public static void splitPer8(String str) {
        int len = str.length();

        if (len == 0) {
            return;
        }

        if (len < 8) {
            int diff = 8 - len;
            for (int i = 0; i < diff; i++) {
                str += "0";
            }
            System.out.println(str);
            return;
        }

        if (len == 8) {
            System.out.println(str);
            return;
        }
        int index = 0;
        int count;
        if (len % 8 == 0) {
            count = len / 8;
        } else {
            count = len/8 + 1;
        }
        char[][] ch = new char[count][8];
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < 8; j++) {
                if (index < len) {
                    ch[i][j] = str.charAt(index);
                    index++;
                } else {
                    ch[i][j] = '0';
                }
            }
        }

        for (int i = 0; i < ch.length; i++) {
            for (int j = 0; j < ch[i].length; j++) {
                System.out.print(ch[i][j]);
            }
            System.out.println();
        }
    }
}
