package com.ft.br.study.algorithm.sort;

/**
 * 排序
 *
 * @author shichunyang
 */
@SuppressWarnings("unchecked")
public class Sort {
	/**
	 * 没有排序的数组
	 */
	public static final Integer[] NO_SORT_ARRAY = {3, 1, 5, 7, 4, 2, 6};

	/**
	 * 判断a是否小于b
	 *
	 * @param a 元素a
	 * @param b 元素b
	 * @return true:是的
	 */
	@SuppressWarnings("all")
	public static boolean less(Comparable a, Comparable b) {
		return a.compareTo(b) < 0;
	}

	/**
	 * 交换索引i,j的元素
	 *
	 * @param arr 数组
	 * @param i   索引i
	 * @param j   索引j
	 */
	public static void exch(Comparable[] arr, int i, int j) {
		Comparable t = arr[i];
		arr[i] = arr[j];
		arr[j] = t;
	}

	/**
	 * 打印数组
	 *
	 * @param arr 数组
	 */
	public static void show(Comparable[] arr) {
		for (Comparable a : arr) {
			System.out.print(a + ", ");
		}
		System.out.println();
	}

	/**
	 * 判断数组是否是有序的
	 *
	 * @param arr 数组
	 * @return true:有序
	 */
	public static boolean isSorted(Comparable[] arr) {
		for (int i = 1; i < arr.length; i++) {
			if (less(arr[i], arr[i - 1])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 选择排序
	 *
	 * @param arr 要排序的数组
	 */
	public static void selectionSort(Comparable[] arr) {
		int length = arr.length;
		for (int i = 0; i < length; i++) {
			// i为当前最小元素的存储位置
			int min = i;

			for (int j = i + 1; j < length; j++) {
				if (less(arr[j], arr[min])) {
					min = j;
				}
			}
			exch(arr, i, min);
		}
	}

	/**
	 * 冒泡排序算法
	 *
	 * @param arr 要排序的数组
	 */
	public static void bubbleSort(Comparable[] arr) {
		int length = arr.length;
		// 剩下的最后一个数不需要比较
		for (int i = 0; i < length - 1; i++) {

			// 本次存储最大元素的索引
			int maxIndex = length - 1 - i;

			// 最大元素所在的索引
			int index = 0;

			for (int j = 0; j < length - 1 - i; j++) {
				if (less(arr[j + 1], arr[j])) {
					if (less(arr[index], arr[j])) {
						index = j;
					}
				}
			}

			exch(arr, index, maxIndex);
		}
	}

	/**
	 * 插入排序
	 *
	 * @param arr 要排序的数组
	 */
	public static void insertionSort(Comparable[] arr) {
		int length = arr.length;
		for (int i = 1; i < length; i++) {
			for (int j = i; j > 0 && less(arr[j], arr[j - 1]); j--) {
				exch(arr, j, j - 1);
			}
		}
	}

	/**
	 * 将二叉堆中元素进行上浮处理
	 *
	 * @param arr 二叉堆
	 * @param k   指定索引(大于1)
	 */
	public static void swim(Comparable[] arr, int k) {
		// k / 2代表父结点
		while (k > 1 && less(arr[k / 2], arr[k])) {
			exch(arr, k / 2, k);
			k = k / 2;
		}
	}

	/**
	 * 将二叉堆中元素进行下沉处理
	 *
	 * @param arr  二叉堆
	 * @param k    指定索引
	 * @param size 二叉堆元素数量
	 */
	public static void sink(Comparable[] arr, int k, int size) {
		while (2 * k <= size) {
			// i为左结点
			int i = 2 * k;
			if (i < size && less(arr[i], arr[i + 1])) {
				// 此时i为右节点
				i++;
			}
			if (!less(arr[k], arr[i])) {
				break;
			}

			exch(arr, k, i);

			k = i;
		}
	}

	/**
	 * 堆排序
	 *
	 * @param arr 要排序的数组
	 */
	public static void heapSort(Comparable[] arr) {
		int length = arr.length;
		Comparable[] heap = new Comparable[length + 1];
		for (int i = 0; i < length; i++) {
			heap[i + 1] = arr[i];
		}
		// 构造堆
		for (int k = length / 2; k >= 1; k--) {
			sink(heap, k, length);
		}

		show(heap);

		// 下沉排序
		while (length > 1) {
			// 将最大元素放置数组最后, 并逻辑移除
			exch(heap, 1, length--);
			// 将新堆使用下沉恢复
			sink(heap, 1, length);
		}

		show(heap);
	}

	public static void main(String[] args) {
		show(NO_SORT_ARRAY);
		show(NO_SORT_ARRAY);
	}
}
