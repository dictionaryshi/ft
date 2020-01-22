package com.ft.br.study.algorithm.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * 排序
 *
 * @author shichunyang
 */
public class Sort {

	private static void exchange(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	/**
	 * 选择排序
	 */
	public List<Integer> sortArray(int[] nums) {
		int length = nums.length;
		for (int i = 0; i < length; i++) {
			// i为当前最小元素的存储位置
			int min = i;

			for (int j = i + 1; j < length; j++) {
				if (nums[j] < nums[min]) {
					min = j;
				}
			}
			exchange(nums, i, min);
		}

		List<Integer> result = new ArrayList<>();
		for (int num : nums) {
			result.add(num);
		}
		return result;
	}
}
