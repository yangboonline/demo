import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * @author yangbo
 * @date 2019/8/27
 */
@Data
@Builder
public class TreeNode {

    private int val;
    private TreeNode left;
    private TreeNode right;

    private TreeNode(int val) {
        this.val = val;
    }

    public TreeNode sortedArrayToBST(int[] nums) {
        if (nums.length == 0) {
            return null;
        }
        return sortedArrayToBST(nums, 0, nums.length - 1);
    }

    private TreeNode sortedArrayToBST(int[] nums, int i, int j) {
        if (i > j) {
            return null;
        }
        int m = i + (j - i) / 2;
        TreeNode root = new TreeNode(nums[m]);
        root.left = sortedArrayToBST(nums, i, m - 1);
        root.right = sortedArrayToBST(nums, m + 1, j);
        return root;
    }

    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        return isMirror(root.left, root.right);
    }

    public boolean isMirror(TreeNode left, TreeNode right) {
        if (left == null) return right == null;
        if (right == null) return left == null;
        return left.val == right.val && isMirror(left.left, right.right) && isMirror(left.right, right.left);
    }

    private static int maxDepth(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int left = maxDepth(node.left);
        int right = maxDepth(node.right);
        return Math.max(left, right) + 1;
    }

    private static List<Integer> preOrderStack(TreeNode root) {
        if (root == null) {
            return null;
        }
        Stack<TreeNode> stack = new Stack<>();
        List<Integer> list = Lists.newArrayList();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            list.add(node.val);
            if (node.right != null) {
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }
        }
        return list;
    }

    private static List<Integer> treeOrder(TreeNode root) {
        if (root == null) {
            return null;
        }
        List<Integer> list = Lists.newArrayList();
        postOrder(root, list);
        return list;
    }

    private static void preOrder(TreeNode root, List<Integer> list) {
        if (root == null) {
            return;
        }
        list.add(root.val);
        preOrder(root.left, list);
        preOrder(root.right, list);
    }

    private static void inOrder(TreeNode root, List<Integer> list) {
        if (root == null) {
            return;
        }
        inOrder(root.left, list);
        list.add(root.val);
        inOrder(root.right, list);
    }

    private static void postOrder(TreeNode root, List<Integer> list) {
        if (root == null) {
            return;
        }
        postOrder(root.left, list);
        postOrder(root.right, list);
        list.add(root.val);
    }

    private static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            result.add(level);
        }
        return result;
    }

    public static void main(String[] args) {
        //               18
        //         14           20
        //     12     15     19     21
        //  10    13
        TreeNode l3 = TreeNode.builder().val(10).build();
        TreeNode r3 = TreeNode.builder().val(13).build();
        TreeNode l1 = TreeNode.builder().val(12).left(l3).right(r3).build();
        TreeNode r1 = TreeNode.builder().val(15).build();
        TreeNode l2 = TreeNode.builder().val(19).build();
        TreeNode r2 = TreeNode.builder().val(21).build();
        TreeNode l0 = TreeNode.builder().val(14).left(l1).right(r1).build();
        TreeNode r0 = TreeNode.builder().val(20).left(l2).right(r2).build();
        TreeNode root = TreeNode.builder().val(18).left(l0).right(r0).build();
        final List<List<Integer>> lists = levelOrder(root);
        System.out.println();
    }

}
