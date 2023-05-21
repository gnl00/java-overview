package hw;

import java.util.Scanner;

/**
 *
 *
 * @author gnl
 * @since 2023/4/14
 */
public class HJ17 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        String[] strings = str.split(";");
        move(strings);
    }

    public static void move(String[] arr) {

        int[] origin = {0, 0};

        for (int i = 0; i < arr.length; i++) {
            if (arr[i].length() == 0) continue;

            String left = arr[i].substring(0, 1);
            String right = arr[i].substring(1);

            int step = 0;

            if (legal(right)) {
                step = Integer.valueOf(right);

                switch (left) {
                    case "A":
                        origin[0] = origin[0] - step;
                        break;
                    case "S":
                        origin[1] = origin[1] - step;
                        break;
                    case "D":
                        origin[0] = origin[0] + step;
                        break;
                    case "W":
                        origin[1] = origin[1] + step;
                        break;
                    default:
                        break;
                }
            }
        }
        System.out.println(origin[0] + "," + origin[1]);

    }

    public static boolean legal(String right) {
        if (right.length() == 0) return false;

        boolean result = true;
        for (int i = 0; i < right.length(); i++) {
            if (!isNum(right.charAt(i))) {
                result = false;
                return result;
            }
        }
        return result;
    }

    public static boolean isNum(char ch) {
        return ch - '0' >= 0 && ch - '0' <= 9;
    }
}
