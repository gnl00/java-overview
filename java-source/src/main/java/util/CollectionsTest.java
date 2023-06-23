package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/3/10
 */
public class CollectionsTest {
    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        Collections.fill(list, "0");
        System.out.println(list);
    }
}
