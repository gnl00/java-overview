package mass;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/3/6
 */
public class LRUCache {
    public static void main(String[] args) {
        LRUCache lru = new LRUCache(4);

        lru.put(1, 1);
        lru.put(2, 2);
        lru.put(3, 3);

        lru.get(1);

        lru.put(4, 4);

        lru.put(5, 5);

        lru.get(1);
    }

    private int size;
    private Map<Integer, Node> cache = null;
    private Node head;
    private Node tail;

    public LRUCache(int capacity) {
        this.size = capacity;
        cache = new HashMap<>();
        this.head = new Node();
    }

    public int get(int key) {
        Node cur = null;

        if ((cur = cache.get(key)) != null) {
            reHot(cur);
            return cur.val;
        }

        return -1;
    }

    public void put(int key, int value) {
        Node cur = null;

        // exists
        if ((cur = cache.get(key)) != null) {
            cur.next = head;
            head.prev = cur;
            cur.val = value;

            reHot(cur);
            return;
        }

        // not exists

        // judge size
        // over size very soon
        if (cache.size() == size) {
            // remove tail
            removeTail();
        }

        Node nd = new Node();
        nd.key = key;
        nd.val = value;

        if (head.next == null) tail = nd;

        nd.next = head;
        head.prev = nd;
        head = nd;

        cache.put(key, nd);
    }

    public void reHot(Node cur) {
        // one node only or cur is head, do nothing
        if (head == tail) return;

        // if is middle
        if(cur.prev != null && cur.next != null) {
            cur.prev.next = cur.next;
            cur.next.prev = cur.prev;
        }

        // if is tail and more than one node
        if(cur == tail){
            tail = tail.prev;
            tail.next = null;
        }

        // insert before head
        cur.next = head;

        // handle head
        head.prev = cur;
        head = cur;
    }

    public void removeTail() {
        cache.remove(tail.key);
        tail = tail.prev;
        tail.next = null;
    }
}

class Node {
    protected Node prev;
    protected Node next;
    protected int key;
    protected int val;

    public Node() {}
}
