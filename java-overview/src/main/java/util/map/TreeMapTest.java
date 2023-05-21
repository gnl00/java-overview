package util.map;

import java.util.Map;
import java.util.TreeMap;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/3/10
 */
public class TreeMapTest {
    public static void main(String[] args) {
        Map<Object, Object> tree = new TreeMap<>();
        tree.put("foo", "bar");
        tree.put(1, 1);
        tree.put("false", "bar");
        tree.put("1.22", 2);
        System.out.println(tree);
    }
}
