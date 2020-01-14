package com.ft.br.study.algorithm.leetcode;

/**
 * 字符串类算法题
 *
 * @author shichunyang
 */
public class StringLeetCode {

	public String longestCommonPrefix(String[] strs) {
		if (strs.length == 0) {
			return "";
		}

		// 拿到第一个串当公共前缀
		String prefix = strs[0];
		for (int i = 1; i < strs.length; i++) {
			// 不存在或者不在头部
			while (strs[i].indexOf(prefix) != 0) {
				prefix = prefix.substring(0, prefix.length() - 1);
				if (prefix.isEmpty()) {
					return "";
				}
			}
		}
		return prefix;
	}
}
