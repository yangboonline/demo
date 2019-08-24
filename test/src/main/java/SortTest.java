import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

@Data
@Builder
public class SortTest {

    public static void main(String[] args) {
        int[] arr1 = {4, 5, 6, 8, 9, 2, 3, 4, 9, 5, 1, 4, 11, 22};
        int[] arr2 = {4, 5, 6, 8, 9, 2, 3, 4, 9, 5, 1, 4, 11, 22};
        //int[] arr = mergeArray(arr1, arr2);
        //mergeSort(arr, 0, arr.length - 1, new int[arr.length]);
        //quickSort(arr, 0, arr.length - 1);
        System.out.println();
    }

    private static void testMapCascadeSort() {
        // 一亿条数据
        int[] arr = {4, 5, 6, 8, 9, 2, 3, 4, 9, 5, 1, 4, 11, 22};
        System.out.println("开始");
        long start = System.currentTimeMillis();
        List<Map.Entry<Integer, Long>> list = mapCascadeSort(arr);
        long end = System.currentTimeMillis();
        System.out.println("间隔" + (end - start));
    }


    /**
     * TopN问题
     */
    private static List<Map.Entry<Integer, Long>> mapCascadeSort(int[] arr) {
        TreeMap<Integer, Long> map = Maps.newTreeMap();
        for (int item : arr) {
            if (map.containsKey(item)) {
                map.put(item, map.get(item) + 1L);
            } else {
                map.put(item, 1L);
            }
        }
        List<Map.Entry<Integer, Long>> list = new ArrayList<>(map.entrySet());
        list.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
        return list;
    }

    /**
     * 从尾部开始,倒序
     */
    private static String reverseStr2(String str) {
        char[] arr = str.toCharArray(); //string转换成char数组
        StringBuilder reverse = new StringBuilder();
        for (int i = arr.length - 1; i >= 0; i--) {
            reverse.append(arr[i]);
        }
        return reverse.toString();
    }

    /**
     * 利用栈:First In Last Out
     */
    private static String reverseStr1(String str) {
        StringBuffer sb = new StringBuffer();
        Stack<Character> s = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            s.add(str.charAt(i));
        }
        for (int i = 0; i < str.length(); i++) {
            sb.append(s.pop());
        }
        return sb.toString();
    }


    /**
     * 二分查找先排序 binarySearch(new int[]{0, 1, 2, 3, 4, 5}, 5)
     */
    private static int binarySearch(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] > target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return -1;
    }


    /**
     * 快排 quickSort(arr, 0, arr.length - 1);
     */
    private static void quickSort(int[] arr, int start, int end) {
        if (start < end) {
            int base = arr[start];
            int i = start;
            int j = end;
            int temp = 0;
            do {
                while (arr[i] < base && i < end) {
                    i++;
                }
                while (arr[j] > base && j > start) {
                    j--;
                }
                if (i <= j) {
                    temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                    i++;
                    j--;
                }
            } while (i <= j);
            if (start < j) {
                quickSort(arr, start, j);
            }
            if (end > i) {
                quickSort(arr, i, end);
            }
        }
    }

    /**
     * 合并数组
     */
    private static int[] mergeArray(int[] arr1, int[] arr2) {
        int[] arr = new int[arr1.length + arr2.length];
        int t = 0;
        int i = 0;
        int j = 0;

        while (i < arr1.length && j < arr2.length) {
            if (arr1[i] <= arr2[j]) {
                arr[t++] = arr1[i++];
            } else {
                arr[t++] = arr1[j++];
            }
        }

        while (i < arr1.length) {
            arr[t++] = arr1[i++];
        }
        while (j < arr2.length) {
            arr[t++] = arr1[j++];
        }
        return arr;
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
                    temp[t++] = arr[i++];
                } else {
                    temp[t++] = arr[j++];
                }
            }

            while (i <= mid) {
                temp[t++] = arr[i++];
            }
            while (j <= right) {
                temp[t++] = arr[j++];
            }

            t = 0;
            int tempLeft = left;
            while (tempLeft <= right) {
                arr[tempLeft] = temp[t++];
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
