package com.ft.br.study.algorithm.leetcode;

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
}
