package hw;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 给定 n 个字符串，请对 n 个字符串按照字典序排列。
 *
 * @author gnl
 * @since 2023/4/14
 */
public class HJ14 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int count = sc.nextInt();
        sc.nextLine();

        List<String> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String str = sc.nextLine();
            list.add(str);
        }

        list.sort(String::compareTo);

        for (String s : list) {
            System.out.println(s);
        }
    }
}
