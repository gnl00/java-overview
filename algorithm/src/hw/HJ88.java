package hw;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 扑克牌大小
 * https://www.nowcoder.com/share/jump/3573155391684247246815
 *
 * @author gnl
 * @since 2023/5/16
 */
public class HJ88 {
    public static Map<String, Integer> pkScore = new HashMap<>();

    static {
        pkScore.put("3", 1);
        pkScore.put("4", 2);
        pkScore.put("5", 3);
        pkScore.put("6", 4);
        pkScore.put("7", 5);
        pkScore.put("8", 6);
        pkScore.put("9", 7);
        pkScore.put("10", 8);
        pkScore.put("J", 9);
        pkScore.put("Q", 10);
        pkScore.put("K", 11);
        pkScore.put("A", 12);
        pkScore.put("2", 13);
        pkScore.put("joker", 14);
        pkScore.put("JOKER", 15);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        String str = in.nextLine();
        compare(str);
    }

    public static void compare(String str) {
        String[] pk = str.split("-");
        String pkStr1 = pk[0];
        String pkStr2 = pk[1];

        if(!comparable(pkStr1, pkStr2)) {
            System.out.println("ERROR");
        }
    }

    public static boolean comparable(String pkStr1, String pkStr2) {
        String[] pk1 = pkStr1.split(" ");
        String[] pk2 = pkStr2.split(" ");

        if (pk1.length != pk2.length && noBoomOrDoubleJoker(pk1, pk2)) {
            return false;
        }

        String output = null;

        // 长度相等比较大小
        if(pk1.length == pk2.length) {
            if(pkScore.get(pk1[0]) > pkScore.get(pk2[0])) {
                output = pkStr1;
            } else {
                output = pkStr2;
            }
        }

        // 长度不相等说明一方存在炸弹或者大小王，输出炸弹或大小王
        if (null == output) {
            // 存在大小王
            if (pk1[0].toLowerCase().equals("joker")) {
                output = pkStr1;
            } else if(pk2[0].toLowerCase().equals("joker")) {
                output = pkStr2;
            }
        }

        // 存在炸弹
        if (null == output) {
            if (pk1.length == 4) {
                output = pkStr1;
            } else if(pk2.length == 4) {
                output = pkStr2;
            }
        }

        System.out.println(output);

        return true;
    }

    public static boolean noBoomOrDoubleJoker(String[] pk1, String[] pk2) {
        return !hasBoomOrDoubleJoker(pk1) && !hasBoomOrDoubleJoker(pk2);
    }

    public static boolean hasBoomOrDoubleJoker(String[] pk) {
        // boom
        if (pk.length == 4 && pk[0].equals(pk[1]) && pk[1].equals(pk[2]) && pk[2].equals(pk[3])) return true;

        // double joker
        if (pk.length == 2 && pk[0].toLowerCase().equals("joker") && pk[1].toLowerCase().equals("joker")) return true;

        return false;
    }
}
