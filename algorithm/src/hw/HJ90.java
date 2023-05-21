package hw;

import java.util.Scanner;

/**
 * 合法IP
 *
 * @author gnl
 * @since 2023/5/17
 */
public class HJ90 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        String str = in.nextLine();
        if(validate(str)) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }
    }

    public static boolean validate(String ipStr) {
        String[] ipArr = ipStr.split("\\.");
        if(ipArr.length != 4) {
            return false;
        }

        for(String seg : ipArr) {
            try {
                if (startsWithNotNum(seg)) return false;

                if (seg.length() > 1 && startsWithZero(seg) ) return false;

                int iSeg = Integer.parseInt(seg);
                if(iSeg < 0 || iSeg > 255) return false;
            } catch (Exception e) {
                return false;
            }

        }
        return true;
    }

    public static boolean startsWithNotNum(String str) {
        char start = str.charAt(0);
        return start < 0 + '0' || start > 9 + '0';
    }

    public static boolean startsWithZero(String str) {
        return str.startsWith("0");
    }
}
