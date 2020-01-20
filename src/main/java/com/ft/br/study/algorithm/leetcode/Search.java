package com.ft.br.study.algorithm.leetcode;

/**
 * 查找
 *
 * @author shichunyang
 */
public class Search {

	/**
	 * 二分查找
	 */
	public int search(int[] nums, int target) {
		int mid, left = 0, right = nums.length - 1;
		while (left <= right) {
			mid = left + (right - left) / 2;
			if (nums[mid] == target) {
				return mid;
			}

			if (nums[mid] > target) {
				right = mid - 1;
			} else {
				left = mid + 1;
			}
		}
		return -1;
	}
}
