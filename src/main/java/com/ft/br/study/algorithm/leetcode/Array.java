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
}
