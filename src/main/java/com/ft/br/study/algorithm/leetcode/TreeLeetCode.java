package com.ft.br.study.algorithm.leetcode;

import java.util.*;

/**
 * TreeLeetCode
 *
 * @author shichunyang
 */
public class TreeLeetCode {

	private class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		TreeNode(int x) {
			val = x;
		}
	}

	/**
	 * 前序遍历(中、左、右)
	 */
	public List<Integer> preorderTraversal(TreeNode root) {
		List<Integer> result = new ArrayList<>();
		this.preorderTraversal(root, result);
		return result;
	}

	private void preorderTraversal(TreeNode node, List<Integer> result) {
		if (node != null) {
			result.add(node.val);
			preorderTraversal(node.left, result);
			preorderTraversal(node.right, result);
		}
	}

	public List<Integer> preorderTraversal2(TreeNode root) {
		LinkedList<TreeNode> stack = new LinkedList<>();
		LinkedList<Integer> output = new LinkedList<>();
		if (root == null) {
			return output;
		}

		stack.add(root);
		while (!stack.isEmpty()) {
			TreeNode node = stack.pollLast();
			output.add(node.val);
			if (node.right != null) {
				stack.add(node.right);
			}
			if (node.left != null) {
				stack.add(node.left);
			}
		}
		return output;
	}

	/**
	 * 中序遍历(左、中、右)
	 */
	public List<Integer> inorderTraversal(TreeNode root) {
		List<Integer> result = new ArrayList<>();
		inorderTraversal(root, result);
		return result;
	}

	private void inorderTraversal(TreeNode node, List<Integer> result) {
		if (node != null) {
			if (node.left != null) {
				inorderTraversal(node.left, result);
			}
			result.add(node.val);
			if (node.right != null) {
				inorderTraversal(node.right, result);
			}
		}
	}

	public List<Integer> inorderTraversal2(TreeNode root) {
		List<Integer> result = new ArrayList<>();
		Stack<TreeNode> stack = new Stack<>();
		TreeNode curr = root;
		while (curr != null || !stack.isEmpty()) {
			while (curr != null) {
				stack.push(curr);
				curr = curr.left;
			}
			curr = stack.pop();
			result.add(curr.val);
			curr = curr.right;
		}
		return result;
	}

	/**
	 * 后序(左、右、中)
	 */
	public List<Integer> postorderTraversal(TreeNode root) {
		List<Integer> result = new ArrayList<>();
		postorderTraversal(root, result);
		return result;
	}

	private void postorderTraversal(TreeNode node, List<Integer> result) {
		if (node != null) {
			postorderTraversal(node.left, result);
			postorderTraversal(node.right, result);
			result.add(node.val);
		}
	}

	public List<Integer> postorderTraversal2(TreeNode root) {
		LinkedList<TreeNode> stack = new LinkedList<>();
		LinkedList<Integer> output = new LinkedList<>();
		if (root == null) {
			return output;
		}

		stack.add(root);
		while (!stack.isEmpty()) {
			TreeNode node = stack.pollLast();
			output.addFirst(node.val);
			if (node.left != null) {
				stack.add(node.left);
			}
			if (node.right != null) {
				stack.add(node.right);
			}
		}
		return output;
	}

	/**
	 * 分层遍历
	 */
	public List<List<Integer>> levelOrder(TreeNode root) {
		List<List<Integer>> levelResult = new ArrayList<>();
		dfs(root, 0, levelResult);
		return levelResult;
	}

	private void dfs(TreeNode root, int level, List<List<Integer>> levelResult) {
		if (root == null) {
			return;
		}

		// 添加一个新的ArrayList表示新的一层
		if (level >= levelResult.size()) {
			levelResult.add(new ArrayList<>());
		}

		levelResult.get(level).add(root.val);
		dfs(root.left, level + 1, levelResult);
		dfs(root.right, level + 1, levelResult);
	}

	/**
	 * 给定一个二叉树, 找到最长的路径, 这个路径中的每个节点具有相同值。 这条路径可以经过也可以不经过根节点。
	 */
	private int ans;

	public int longestUnivaluePath(TreeNode root) {
		ans = 0;
		arrowLength(root);
		return ans;
	}

	public int arrowLength(TreeNode node) {
		if (node == null) {
			return 0;
		}
		int left = arrowLength(node.left);
		int right = arrowLength(node.right);
		int arrowLeft = 0, arrowRight = 0;
		if (node.left != null && node.left.val == node.val) {
			arrowLeft += left + 1;
		}
		if (node.right != null && node.right.val == node.val) {
			arrowRight += right + 1;
		}
		ans = Math.max(ans, arrowLeft + arrowRight);
		return Math.max(arrowLeft, arrowRight);
	}

	/**
	 * 给定一棵二叉树, 计算它的直径长度。一棵二叉树的直径长度是任意两个结点路径长度中的最大值。这条路径可能穿过根结点。
	 */
	public int diameterOfBinaryTree(TreeNode root) {
		ans = 1;
		depth(root);
		return ans - 1;
	}

	public int depth(TreeNode node) {
		if (node == null) {
			return 0;
		}
		int L = depth(node.left);
		int R = depth(node.right);
		ans = Math.max(ans, L + R + 1);
		return Math.max(L, R) + 1;
	}

	/**
	 * 给定一个二叉树和一个目标和, 判断该树中是否存在根节点到叶子节点的路径, 这条路径上所有节点值相加等于目标和。
	 */
	public boolean hasPathSum(TreeNode root, int sum) {
		if (root == null) {
			return false;
		}

		sum -= root.val;
		if ((root.left == null) && (root.right == null)) {
			return (sum == 0);
		}
		return hasPathSum(root.left, sum) || hasPathSum(root.right, sum);
	}

	private Map<Integer, Integer> depth;
	private Map<Integer, TreeNode> parent;

	public boolean isCousins(TreeNode root, int x, int y) {
		depth = new HashMap<>(16);
		parent = new HashMap<>(16);
		dfs(root, null);
		return (depth.get(x).equals(depth.get(y)) && parent.get(x) != parent.get(y));
	}

	public void dfs(TreeNode node, TreeNode par) {
		if (node != null) {
			depth.put(node.val, par != null ? 1 + depth.get(par.val) : 0);
			parent.put(node.val, par);
			dfs(node.left, node);
			dfs(node.right, node);
		}
	}
}
