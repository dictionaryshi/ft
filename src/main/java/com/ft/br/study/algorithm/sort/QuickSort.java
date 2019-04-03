package com.ft.br.study.algorithm.sort;

/**
 * 快速排序
 *
 * @author shichunyang
 */
@SuppressWarnings("unchecked")
public class QuickSort {
	public static void quickSort(Comparable[] arr) {
		if (arr == null || arr.length < 2) {
			return;
		}
		quickSort(arr, 0, arr.length - 1);
	}

	private static void quickSort(Comparable[] arr, int low, int high) {

		if (low < high) {
			// 找寻基准数据的正确索引
			int index = getIndex(arr, low, high);

			// 进行迭代对index之前和之后的数组进行相同的操作使整个数组变成有序
			quickSort(arr, 0, index - 1);
			quickSort(arr, index + 1, high);
		}

	}

	private static int getIndex(Comparable[] arr, int low, int high) {
		// 基准数据
		Comparable tmp = arr[low];
		while (low < high) {
			// 当队尾的元素大于等于基准数据时,向前挪动high指针
			while (low < high && arr[high].compareTo(tmp) >= 0) {
				high--;
			}
			// 如果队尾元素小于tmp了, 将其赋值给low
			arr[low] = arr[high];
			// 当队首元素小于等于tmp时, 挪动low指针
			while (low < high && arr[low].compareTo(tmp) <= 0) {
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

	public static void main(String[] args) {
		Sort.show(Sort.NO_SORT_ARRAY);
		quickSort(Sort.NO_SORT_ARRAY);
		Sort.show(Sort.NO_SORT_ARRAY);
	}
}
