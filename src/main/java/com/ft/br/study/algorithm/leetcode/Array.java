package com.ft.br.study.algorithm.leetcode;

/**
 * 数组类题目
 *
 * @author shichunyang
 */
public class Array {

	/**
	 * 删除排序数组中的重复项
	 */
	public int removeDuplicates(int[] nums) {
		if (nums.length == 0) {
			return 0;
		}

		int i = 0;
		for (int j = 1; j < nums.length; j++) {
			if (nums[j] != nums[i]) {
				i++;
				nums[i] = nums[j];
			}
		}
		return i + 1;
	}

	/**
	 * 查找两个有序数组的中位
	 */
	public double findMedianSortedArrays(int[] a, int[] b) {
		int m = a.length;
		int n = b.length;
		int len = m + n;
		int left = -1, right = -1;
		int aStart = 0, bStart = 0;
		for (int i = 0; i <= len / 2; i++) {
			left = right;
			if (aStart < m && (bStart >= n || a[aStart] < b[bStart])) {
				// a没走完 && (b走完了 || a < b)
				right = a[aStart++];
			} else {
				right = b[bStart++];
			}
		}
		if ((len & 1) == 0) {
			return (left + right) / 2.0;
		} else {
			return right;
		}
	}
}
