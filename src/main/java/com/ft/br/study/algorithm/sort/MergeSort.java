package com.ft.br.study.algorithm.sort;

/**
 * 归并排序
 *
 * @author shichunyang
 */
public class MergeSort {
	public static void main(String[] args) {
		Integer[] arr = Sort.NO_SORT_ARRAY;
		Sort.show(arr);
		sort(arr);
		Sort.show(arr);
	}

	public static void sort(Comparable[] arr) {
		Comparable[] temp = new Comparable[arr.length];
		sort(arr, 0, arr.length - 1, temp);
	}

	private static void sort(Comparable[] arr, int start, int end, Comparable[] temp) {
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

	private static void merge(Comparable[] arr, int start, int mid, int end, Comparable[] temp) {
		// 左序列指针
		int leftIndex = start;
		// 右序列指针
		int rightIndex = mid + 1;
		// 临时数组指针
		int tempIndex = 0;
		while (leftIndex <= mid && rightIndex <= end) {
			if (Sort.less(arr[leftIndex], arr[rightIndex])) {
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
}
