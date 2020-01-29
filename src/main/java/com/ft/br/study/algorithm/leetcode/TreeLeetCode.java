package com.ft.br.study.algorithm.leetcode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
}
