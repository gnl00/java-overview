package lambda;

import java.util.function.BiFunction;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/3/14
 */
public class BiFunctionTest {

    public static void main(String[] args) {
        Integer res = compute(1, 2, (a, b) -> a + b);
        System.out.println(res);
    }

    static <T> T compute(int numa, int numb, BiFunction<Integer, Integer, T> action) {
        return action.apply(numa, numb);
    }
}
