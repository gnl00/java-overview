package hw;

import java.util.Scanner;

/**
 * 输出单向链表中倒数第k个结点
 * https://www.nowcoder.com/share/jump/3573155391684247264246
 *
 * @author gnl
 * @since 2023/5/16
 */
public class HJ51 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别

        int len = 0;
        String str = null;
        int reverseIndex = 0;

        int count = 0;
        while (in.hasNext()) {
            if (count % 3 == 0) {
                len = in.nextInt();
                in.nextLine();
            }
            if (count % 3 == 1) {
                str = in.nextLine();
            }
            if (count % 3 == 2) {
                reverseIndex = in.nextInt();

                Node51 root = buildList(str);
                findAndRemove(root, len, reverseIndex);
            }

            count++;
        }
    }

    public static void findAndRemove(Node51 head, int len, int reverseIndex) {
        if (len == reverseIndex) {
            System.out.println(head.data);
            return;
        }

        int removeIndex = len - reverseIndex;
        int i = 1;

        Node51 cur = head;
        while (i != removeIndex) {
            cur = cur.next;
            i++;
        }

        // do remove
        System.out.println(cur.next.data);
    }
    
    public static Node51 buildList(String str) {
        String[] arr = str.split(" ");
        Node51 head = new Node51(Integer.parseInt(arr[0]), null);
        Node51 cur = head;
        int i = 1;
        while (i < arr.length) {

            cur.next = new Node51(Integer.parseInt(arr[i]), null);
            cur = cur.next;

            i++;
        }

        return head;
    }
}

class Node51 {
    int data;
    Node51 next;

    public Node51(int data, Node51 next) {
        this.data = data;
        this.next = next;
    }

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                ", next=" + next +
                '}';
    }
}
