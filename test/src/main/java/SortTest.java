import java.util.Arrays;

public class SortTest {

    public static void main(String[] args) {
        int[] arr = {21, 23, 4, 5, 2, 5, 3, 9, 0, 1, 2};
        Arrays.stream(arr).forEach(t -> System.out.print(t + " "));
        System.out.println();
        bubbleSort(arr);
        Arrays.stream(arr).forEach(t -> System.out.print(t + " "));
    }

    /**
     * 归并 mergeSort(arr, 0, arr.length - 1, new int[arr.length]);
     */
    private static void mergeSort(int[] arr, int left, int right, int[] temp) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(arr, left, mid, temp);
            mergeSort(arr, mid + 1, right, temp);
            int i = left;
            int j = mid + 1;
            int t = 0;

            while (i <= mid && j <= right) {
                if (arr[i] <= arr[j]) {
                    temp[t] = arr[i];
                    i++;
                    t++;
                } else {
                    temp[t] = arr[j];
                    j++;
                    t++;
                }
            }

            while (i <= mid) {
                temp[t] = arr[i];
                i++;
                t++;
            }
            while (j <= right) {
                temp[t] = arr[j];
                j++;
                t++;
            }

            t = 0;
            int tempLeft = left;
            while (tempLeft <= right) {
                arr[tempLeft] = temp[t];
                t++;
                tempLeft++;
            }
        }
    }

    /**
     * 冒泡 bubbleSort(arr);
     */
    private static void bubbleSort(int[] arr) {
        int temp;
        boolean flag = false;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    flag = true;
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
            if (!flag) {
                break;
            } else {
                flag = false;
            }
        }
    }

}
