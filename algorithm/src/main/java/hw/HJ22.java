package hw;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * https://www.nowcoder.com/share/jump/3573155391684311650686
 *
 * @author gnl
 * @since 2023/5/17
 */
public class HJ22 {

    public static List<Integer> cache = new LinkedList<>();

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextLine()) { // 注意 while 处理多个 case
            int num = in.nextInt();
            if (num == 0) {
                output();
                break;
            } else {
                int count = bottle(num);
                cache.add(count);
            }
        }
    }

    public static int bottle(int n) {
        int base = 3;

        if (n < 2) return 0;

        if (n == 2) {
            return 1;
        }

        // if n > 2

        // 可以换几瓶
        int count = 0;
        // 剩几个空瓶
        int left;
        if (n % base == 0) {
            count = left = n / base;
        } else {
            count = n / base;
            left = n % base + count;
        }
        count  = count + bottle(left);

        return count;
    }

    public static void output() {
        for (Integer n : cache) {
            System.out.println(n);
        }
    }
}
