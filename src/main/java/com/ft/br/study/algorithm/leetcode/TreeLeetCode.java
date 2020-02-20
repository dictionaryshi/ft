package com.ft.br.study.algorithm.leetcode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

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
}