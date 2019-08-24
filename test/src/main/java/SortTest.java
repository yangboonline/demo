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
        testMapCascadeSort();
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
        /*map.entrySet().stream()
                       .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(10).collect(Collectors.toList())*/
    }

    //从尾部开始,倒序
    private static String reverse3(String str) {
        char[] arr = str.toCharArray(); //string转换成char数组
        StringBuilder reverse = new StringBuilder();
        for (int i = arr.length - 1; i >= 0; i--) {
            reverse.append(arr[i]);
        }
        return reverse.toString();
    }

    //利用栈:First In Last Out
    //java中不用手动销毁
    private static String reverse4(String str) {
        StringBuffer sb = new StringBuffer();
        Stack<Character> s = new Stack<>();

        for (int i = 0; i < str.length(); i++)
            s.add(str.charAt(i));

        for (int i = 0; i < str.length(); i++)
            sb.append(s.pop());

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
    private static void quickSort(int[] arr, int left, int right) {
        int l = left;
        int r = right;
        int pivod = arr[(left + right) / 2];
        int temp;
        while (l < r) {
            while (arr[l] < pivod) {
                l++;
            }
            while (arr[r] > pivod) {
                r--;
            }
            if (l >= r) {
                break;
            }
            temp = arr[l];
            arr[l] = arr[r];
            arr[r] = temp;
            if (arr[l] == pivod) {
                r--;
            }
            if (arr[r] == pivod) {
                l--;
            }
        }
        if (l == r) {
            l++;
            r--;
        }
        if (left < r) {
            quickSort(arr, left, r);
        }
        if (right > l) {
            quickSort(arr, l, right);
        }
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
