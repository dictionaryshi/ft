package com.ft.br.study.algorithm.find;

import lombok.Data;

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
	 * 前序
	 */
	public void preOrder(BinaryTreeNode root) {
		if (null != root) {
			System.out.print(root.getData() + "\t");
			preOrder(root.getLeft());
			preOrder(root.getRight());
		}
	}

	/**
	 * 中序
	 */
	public void inOrder(BinaryTreeNode root) {
		if (null != root) {
			inOrder(root.getLeft());
			System.out.print(root.getData() + "\t");
			inOrder(root.getRight());
		}
	}

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
}
