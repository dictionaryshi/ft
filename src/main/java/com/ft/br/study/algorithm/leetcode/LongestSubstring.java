package com.ft.br.study.algorithm.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * 最长无重复子串长度
 *
 * @author shichunyang
 */
public class LongestSubstring {
	public static int lengthOfLongestSubstring(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		char[] chars = s.toCharArray();
		int len = chars.length;
		int p = 0, q = 0;
		int max = 0;
		Map<Character, Integer> map = new HashMap<>(16);
		while (q < len) {
			if (map.containsKey(chars[q])) {
				// 防止p指针回溯
				p = Math.max(p, map.get(chars[q]) + 1);
			}

			map.put(chars[q], q);
			max = Math.max(max, q - p + 1);
			++q;
		}

		return max;
	}

	public static void main(String[] args) {
		System.out.print(lengthOfLongestSubstring("abccdcef"));
	}
}
