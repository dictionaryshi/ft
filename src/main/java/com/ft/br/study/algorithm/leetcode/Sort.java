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

	/**
	 * 冒泡排序
	 */
	public List<Integer> sortArray2(int[] nums) {
		int length = nums.length;
		// 剩下的最后一个数不需要比较
		for (int i = 0; i < length - 1; i++) {

			for (int j = 0; j < length - 1 - i; j++) {
				if (nums[j + 1] < nums[j]) {
					exchange(nums, j, j + 1);
				}
			}

		}

		List<Integer> result = new ArrayList<>();
		for (int num : nums) {
			result.add(num);
		}
		return result;
	}

	/**
	 * 堆排序
	 */
	public List<Integer> sortArray3(int[] nums) {
		int size = nums.length;
		int[] heap = new int[size + 1];
		for (int i = 0; i < size; i++) {
			heap[i + 1] = nums[i];
		}
		// 构造堆
		for (int parentIndex = size / 2; parentIndex >= 1; parentIndex--) {
			sink(heap, parentIndex, size);
		}

		// 下沉排序
		while (size > 1) {
			// 将最大元素放置数组最后, 并逻辑移除
			exchange(heap, 1, size--);
			// 将新堆使用下沉恢复
			sink(heap, 1, size);
		}

		List<Integer> result = new ArrayList<>();
		for (int i = 1; i < heap.length; i++) {
			result.add(heap[i]);
		}
		return result;
	}

	private static void sink(int[] arr, int parentIndex, int size) {
		while (2 * parentIndex <= size) {
			// 左结点
			int childIndex = 2 * parentIndex;
			if (childIndex < size && arr[childIndex] < arr[childIndex + 1]) {
				// 右节点
				childIndex++;
			}
			if (arr[parentIndex] > arr[childIndex]) {
				break;
			}

			exchange(arr, parentIndex, childIndex);

			parentIndex = childIndex;
		}
	}
}
