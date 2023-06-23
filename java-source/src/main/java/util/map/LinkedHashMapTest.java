package util.map;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/4/15
 */
public class LinkedHashMapTest {
    public static void main(String[] args) {
        Map<String, Object> lm = new LinkedHashMap<>();
        lm.put("1", 1);
        lm.put("2", 2);
        lm.put("3", 3);
        lm.put("4", 4);
        System.out.println(lm);
    }
}
