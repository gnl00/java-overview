package sort;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/3/28
 */
public class CommonSort {
    public static void main(String[] args) {
        CommonSort sort = new CommonSort();
        int[] a = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        sort.bubbleSort(a);
        System.out.println(a[0]);
    }

    public void bubbleSort(int[] arr) {
       arr[0] = 111;
    }
}
