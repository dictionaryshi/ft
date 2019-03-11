package com.ft.study.algorithm.leetcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode1
 *
 * @author shichunyang
 */
public class LeetCode1 {
	public static int[] twoSum(int[] numbers, int target) {
		Map<Integer, Integer> cache = new HashMap<>(16);
		int[] chs = new int[2];
		for (int i = 0; i < numbers.length; i++) {
			int find = target - numbers[i];
			if (cache.containsKey(find)) {
				chs[0] = cache.get(find);
				chs[1] = i;
				break;
			}
			cache.put(numbers[i], i);
		}
		return chs;
	}

	public static void main(String[] args) {
		int[] numbers = {2, 7, 11, 15};
		System.out.println(Arrays.toString(twoSum(numbers, 9)));
	}
}
