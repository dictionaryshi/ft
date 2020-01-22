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
	 * @param arr         二叉堆
	 * @param parentIndex 父索引
	 * @param size        二叉堆元素数量
	 */
	public static void sink(Comparable[] arr, int parentIndex, int size) {
		while (2 * parentIndex <= size) {
			// 左结点
			int childIndex = 2 * parentIndex;
			if (childIndex < size && less(arr[childIndex], arr[childIndex + 1])) {
				// 右节点
				childIndex++;
			}
			if (!less(arr[parentIndex], arr[childIndex])) {
				break;
			}

			exch(arr, parentIndex, childIndex);

			parentIndex = childIndex;
		}
	}

	/**
	 * 堆排序
	 *
	 * @param arr 要排序的数组
	 */
	public static void heapSort(Comparable[] arr) {
		int size = arr.length;
		Comparable[] heap = new Comparable[size + 1];
		for (int i = 0; i < size; i++) {
			heap[i + 1] = arr[i];
		}
		// 构造堆
		for (int parentIndex = size / 2; parentIndex >= 1; parentIndex--) {
			sink(heap, parentIndex, size);
		}

		show(heap);

		// 下沉排序
		while (size > 1) {
			// 将最大元素放置数组最后, 并逻辑移除
			exch(heap, 1, size--);
			// 将新堆使用下沉恢复
			sink(heap, 1, size);
		}

		show(heap);
	}

	public static void main(String[] args) {
		show(NO_SORT_ARRAY);
		show(NO_SORT_ARRAY);
	}
}
