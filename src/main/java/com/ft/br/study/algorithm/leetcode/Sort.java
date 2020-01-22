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
	 * 归并排序
	 */
	public List<Integer> sortArray3(int[] nums) {
		int[] temp = new int[nums.length];
		sort(nums, 0, nums.length - 1, temp);
		List<Integer> result = new ArrayList<>();
		for (int num : nums) {
			result.add(num);
		}
		return result;
	}

	private static void sort(int[] arr, int start, int end, int[] temp) {
		if (start < end) {
			int mid = start + (end - start) / 2;
			// 左边归并排序, 使得左子序列有序
			sort(arr, start, mid, temp);
			// 右边归并排序, 使得右子序列有序
			sort(arr, mid + 1, end, temp);

			// 将两个有序子数组合并操作
			merge(arr, start, mid, end, temp);
		}
	}

	private static void merge(int[] arr, int start, int mid, int end, int[] temp) {
		// 左序列指针
		int leftIndex = start;
		// 右序列指针
		int rightIndex = mid + 1;
		// 临时数组指针
		int tempIndex = 0;
		while (leftIndex <= mid && rightIndex <= end) {
			if (arr[leftIndex] < arr[rightIndex]) {
				temp[tempIndex++] = arr[leftIndex++];
			} else {
				temp[tempIndex++] = arr[rightIndex++];
			}
		}
		// 将左边剩余元素填充进temp中
		while (leftIndex <= mid) {
			temp[tempIndex++] = arr[leftIndex++];
		}
		// 将右序列剩余元素填充进temp中
		while (rightIndex <= end) {
			temp[tempIndex++] = arr[rightIndex++];
		}
		tempIndex = 0;
		// 将temp中的元素全部拷贝到原数组中
		while (start <= end) {
			arr[start++] = temp[tempIndex++];
		}
	}

	/**
	 * 快速排序
	 */
	public List<Integer> sortArray4(int[] nums) {
		quickSort(nums, 0, nums.length - 1);
		List<Integer> result = new ArrayList<>();
		for (int num : nums) {
			result.add(num);
		}
		return result;
	}

	private void quickSort(int[] arr, int low, int high) {
		if (low < high) {
			// 找寻基准数据的正确索引
			int index = getIndex(arr, low, high);

			// 进行迭代对index之前和之后的数组进行相同的操作使整个数组变成有序
			quickSort(arr, 0, index - 1);
			quickSort(arr, index + 1, high);
		}
	}

	private int getIndex(int[] arr, int low, int high) {
		// 基准数据
		int tmp = arr[low];
		while (low < high) {
			// 当队尾的元素大于等于基准数据时,向前挪动high指针
			while (low < high && arr[high] >= tmp) {
				high--;
			}
			// 如果队尾元素小于tmp了, 将其赋值给low
			arr[low] = arr[high];
			// 当队首元素小于等于tmp时, 挪动low指针
			while (low < high && arr[low] <= tmp) {
				low++;
			}
			// 当队首元素大于tmp时, 需要将其赋值给high
			arr[high] = arr[low];

		}
		// 跳出循环时low和high相等, 此时的low或high就是tmp的正确索引位置
		arr[low] = tmp;

		// 返回tmp的正确位置
		return low;
	}
}
