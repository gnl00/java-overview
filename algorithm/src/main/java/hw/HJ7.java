package hw;

import java.util.Scanner;

/**
 * 写出一个程序，接受一个正浮点数值，输出该数值的近似整数值。如果小数点后数值大于等于 0.5 ,向上取整；小于 0.5 ，则向下取整。
 *
 * @author gnl
 * @since 2023/4/14
 */
public class HJ7 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        float f = sc.nextFloat();
        System.out.println(getApproximate(f));
    }

    public static int getApproximate(float f) {
        float result = f * 10 % 10;
        return (int) (result >= 5 ? Math.ceil(f) :  Math.floor(f));
    }
}
