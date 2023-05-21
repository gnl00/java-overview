package hw;

import java.util.Scanner;

/**
 * 从单向链表中删除指定值的节点
 * https://www.nowcoder.com/share/jump/3573155391684247287859
 *
 * @author gnl
 * @since 2023/5/16
 */
public class HJ48 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        String str = in.nextLine();
        removeFromSingleList(str);
    }

    public static void removeFromSingleList(String str) {
        String[] arr = str.split(" ");
        int len = arr.length;
        int headVal = Integer.parseInt(arr[1]);
        Node48 root = new Node48(headVal, null);
        int removeVal = Integer.parseInt(arr[len - 1]);

        Node48 head = buildList(arr, root);

        Node48 result = findAndRemove(head, removeVal);
        output(result);
    }

    public static void output(Node48 root) {
        while (root != null) {
            if (null == root.next) {
                System.out.print(root.data);
            } else {
                System.out.print(root.data + " ");
            }
            root = root.next;
        }
    }

    // 6 2 1 2 3 2 5 1 4 5 7 2 2
    public static Node48 buildList(String[] arr, Node48 head) {

        // check if root is null

        int i = 2;
        while (i + 1 < arr.length - 1) {
            int j = i + 1;
            int next = Integer.parseInt(arr[i]);
            int prev = Integer.parseInt(arr[j]);
            findAndAttach(head, prev, next);
            i++;
        }

        return head;
    }

    public static void findAndAttach(Node48 root, int target, int next) {
        Node48 cur = root;
        while (cur != null) {

            if (null == cur.next && target == cur.data) { // cur => null
                cur.next = new Node48(next, null);
            } else if (null != cur.next && target == cur.data) { // cur => someNode => null
                Node48 nd = new Node48(next, null);
                nd.next = cur.next;
                cur.next = nd;
            }

            cur = cur.next;
        }

    }

    private static Node48 findAndRemove(Node48 head, int removeVal) {
        // head == null
        if (null == head) {
            return null;
        }

        // head => null
        if (head.data == removeVal && head.next == null) {
            return null;
        }

        // head => someNode => null
        if (head.data == removeVal) {
            return head.next;
        }

        Node48 cur = head;
        while (cur.next != null) {
            if (cur.next.data == removeVal) {
                cur.next = cur.next.next;
                break;
            }
            cur = cur.next;
        }
        return head;
    }
}

class Node48 {
    int data;
    Node48 next;

    public Node48(int data, Node48 nt) {
        this.data = data;
        this.next = nt;
    }

    @Override
    public String toString() {
        return "Node{"  + "data: " + this.data + ", next: " + this.next +"}";
    }
}
