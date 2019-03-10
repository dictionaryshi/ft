package com.ft.study.algorithm.find;

/**
 * 查找
 *
 * @author shichunyang
 */
public class Find {
	public static int rank(int key, int[] arr) {
		int low = 0;
		int high = arr.length - 1;
		while (low <= high) {
			int mid = low + (high - low) / 2;
			if (key < arr[mid]) {
				high = mid - 1;
			} else if (key > arr[mid]) {
				low = mid + 1;
			} else {
				return mid;
			}
		}
		return -1;
	}

	/**
	 * 二分查找
	 *
	 * @param key  查找的值
	 * @param arr  数组
	 * @param low  查找的开始位置
	 * @param high 查找的最后位置
	 * @return 查找的索引位置
	 */
	public static int rank(int key, int[] arr, int low, int high) {

		if (low > high) {
			return -1;
		}

		// 中间元素的索引
		int mid = low + (high - low) / 2;

		if (key < arr[mid]) {
			return rank(key, arr, low, mid - 1);
		} else if (key > arr[mid]) {
			return rank(key, arr, mid + 1, high);
		} else {
			return mid;
		}
	}

	public static void main(String[] args) {
		int[] arr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		System.out.println(rank(9, arr));
		System.out.println(rank(3, arr, 0, arr.length - 1));
	}
}
