package com.ft.study.algorithm.leetcode;

import org.apache.commons.lang3.StringUtils;

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

	public static String bigNumberPlus(String numberA, String numberB) {
		int lengthA = numberA.length();
		int lengthB = numberB.length();
		if (lengthA > lengthB) {
			numberB = StringUtils.leftPad(numberB, lengthA, "0");
		} else {
			numberA = StringUtils.leftPad(numberA, lengthB, "0");
		}

		int[] tempCache = new int[numberA.length() + 1];

		for (int i = numberA.length() - 1; i >= 0; i--) {
			int a = Integer.parseInt(numberA.charAt(i) + "");
			int b = Integer.parseInt(numberB.charAt(i) + "");
			int c = tempCache[i + 1];
			int t = a + b + c;
			tempCache[i + 1] = t % 10;
			tempCache[i] = t / 10;
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < tempCache.length; i++) {
			if (i == 0 && tempCache[i] == 0) {
				continue;
			}
			sb.append(tempCache[i]);
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		int[] numbers = {2, 7, 11, 15};
		System.out.println(Arrays.toString(twoSum(numbers, 9)));
		System.out.println(bigNumberPlus("37", "163"));
	}
}