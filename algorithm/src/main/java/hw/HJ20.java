package hw;

import java.util.Scanner;

/**
 * 密码要求:
 * 1.长度超过 8 位
 * 2.包括大小写字母.数字.其它符号,以上四种至少三种
 * 3.不能有长度大于 2 的包含公共元素的子串重复 （注：其他符号不含空格或换行）
 *
 * @author gnl
 * @since 2023/4/15
 */
public class HJ20 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String str = in.nextLine();
            str = str.replace(" ", "").replace("\n", "");
            check(str);
        }
    }

    public static void check(String str) {
        // 长度小于 8
        if (str.length() < 8) {
            System.out.println("NG");
            return;
        }

        // 不能有长度大于 2 的包含公共元素的子串重复
        if (hasSub3(str)) {
            System.out.println("NG");
            return;
        }

        // 大写、小写、数字、字符串 至少包含三种
        // (?=(.*[A-Z]){1,}) 至少包含一个大写
        // (?=(.*[a-z]){1,}) 至少包含一个小写
        // (?=(.*\d){1,}) 至少包含一个数字
        // (?=(.*\W){1,}) 至少包含一个字符
        // .{8,} 前面的内容至少 8 位
        String atLeast3 = "^((?=(.*[A-Z]){1,})|(?=(.*[a-z]){1,})|(?=(.*\\d){1,})|(?=(.*\\W){1,})).{8,}$";
        if (!str.matches(atLeast3)) {
            System.out.println("NG");
            return;
        }

        System.out.println("OK");
    }

    public static boolean hasSub3(String str) {
        int start = 0, end = 0;
        int step = 3;
        while ((end = start + step) < str.length()) {
            String sub = str.substring(start, end);
            if (str.indexOf(sub) != str.lastIndexOf(sub)) {
                return true;
            }
            start++;
        }
        return false;
    }
}
