package hw;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 写出一个程序，接受一个十六进制的数，输出该数值的十进制表示
 *
 * 数据范围：保证结果在 1≤n≤2^31−1
 *
 * @author gnl
 * @since 2023/4/14
 */
public class HJ5 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String hex = sc.nextLine();
        hexToDecimal(hex);
    }

    public static void hexToDecimalNative(String hex) {
        hex = hex.startsWith("0x") ? hex.substring(2) : hex;
        System.out.println(Integer.parseInt(hex, 16));
    }

    public static void hexToDecimal(String str) {
        Map<Object, Integer> map = new HashMap<>();
        map.put('0', 0);
        map.put('1', 1);
        map.put('2', 2);
        map.put('3', 3);
        map.put('4', 4);
        map.put('5', 5);
        map.put('6', 6);
        map.put('7', 7);
        map.put('8', 8);
        map.put('9', 9);
        map.put('A', 10);
        map.put('B', 11);
        map.put('C', 12);
        map.put('D', 13);
        map.put('E', 14);
        map.put('F', 15);
        map.put('a', 10);
        map.put('b', 11);
        map.put('c', 12);
        map.put('d', 13);
        map.put('e', 14);
        map.put('f', 15);

        String hexStr = str.startsWith("0x") ? str.substring(2) : str;
        int hexResult = 0;

        for (char ch : hexStr.toCharArray()) {
            hexResult = hexResult * 16 + map.get(ch);
        }

        System.out.println(hexResult);
    }
}
