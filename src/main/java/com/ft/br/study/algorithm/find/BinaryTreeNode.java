package com.ft.br.study.algorithm.find;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 二叉树
 *
 * @author shichunyang
 */
@Data
public class BinaryTreeNode<T> {
	private Comparable<T> data;
	private BinaryTreeNode left;
	private BinaryTreeNode right;

	/**
	 * 后序
	 */
	public void postOrder(BinaryTreeNode root) {
		if (root != null) {
			postOrder(root.getLeft());
			postOrder(root.getRight());
			System.out.print(root.getData() + "\t");
		}
	}

	/**
	 * 元素个数
	 */
	public static int size(BinaryTreeNode root) {
		if (root == null) {
			return 0;
		} else {
			return size(root.left) + size(root.right) + 1;
		}
	}

	/**
	 * 二叉树高度(深度)
	 */
	public static int deep(BinaryTreeNode root) {
		if (root == null) {
			return 0;
		}

		int leftDeep = deep(root.left);
		int rightDeep = deep(root.right);
		return Math.max(leftDeep, rightDeep) + 1;
	}

	/**
	 * 分层遍历
	 */
	public static void levelPrint(BinaryTreeNode root) {
		List<List<Comparable>> levelResult = new ArrayList<>();
		dfs(root, 0, levelResult);
		System.out.println(levelResult);
	}

	private static void dfs(BinaryTreeNode root, int level, List<List<Comparable>> levelResult) {
		if (root == null) {
			return;
		}

		// 添加一个新的ArrayList表示新的一层
		if (level >= levelResult.size()) {
			levelResult.add(new ArrayList<>());
		}

		levelResult.get(level).add(root.data);
		dfs(root.left, level + 1, levelResult);
		dfs(root.right, level + 1, levelResult);
	}
}
