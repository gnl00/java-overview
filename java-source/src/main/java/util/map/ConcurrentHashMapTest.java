package util.map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/3/28
 */
public class ConcurrentHashMapTest {
    public static void main(String[] args) {
        Map<String, Object> map = new ConcurrentHashMap<String, Object>();
        map.put("key", "value");
        map.get("key");
    }
}
