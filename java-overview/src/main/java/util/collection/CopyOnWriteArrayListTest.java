package util.collection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/3/9
 */
public class CopyOnWriteArrayListTest {
    public static void main(String[] args) {
        CopyOnWriteArrayList<String> cowArray = new CopyOnWriteArrayList<>(new ArrayList<>());
        CopyOnWriteArrayList<String> cowLink = new CopyOnWriteArrayList<>(new LinkedList<>());
    }
}
