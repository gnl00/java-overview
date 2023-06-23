package util.map;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/3/9
 */
public class HashMapTest {
    public static void main(String[] args) {
        Map<Object, Object> map = new HashMap<>();
        map.put("key", "value");
        map.put(1, 2);
        map.put(false, true);
        map.put(1.23, 4.56);
        map.put(null, null);
        System.out.println(map);
        /*for (int i = 1; i < 100; i++) {
            map.put(Integer.toString(i), i);
        }*/

    }
}
