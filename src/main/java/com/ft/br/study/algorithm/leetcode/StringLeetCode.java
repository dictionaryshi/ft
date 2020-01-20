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

	public String longestCommonPrefix2(String[] strs) {
		if (strs == null || strs.length == 0) {
			return "";
		}
		int minLen = Integer.MAX_VALUE;
		for (String str : strs) {
			minLen = Math.min(minLen, str.length());
		}
		int low = 1;
		int high = minLen;
		while (low <= high) {
			int middle = (low + high) / 2;
			if (isCommonPrefix(strs, middle)) {
				low = middle + 1;
			} else {
				high = middle - 1;
			}
		}
		return strs[0].substring(0, (low + high) / 2);
	}

	private boolean isCommonPrefix(String[] strs, int len) {
		String prefix = strs[0].substring(0, len);
		for (int i = 1; i < strs.length; i++) {
			if (!strs[i].startsWith(prefix)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 字符串关键字加粗
	 */
	public String boldWords(String[] words, String str) {
		boolean[] isBold = new boolean[str.length()];
		for (String word : words) {
			int n = str.indexOf(word, 0);
			while (n != -1) {
				for (int i = n; i < n + word.length(); i++) {
					isBold[i] = true;
				}
				n = str.indexOf(word, n + 1);
			}
		}
		StringBuilder sb = new StringBuilder();
		if (isBold[0]) {
			sb.append("<b>");
		}
		for (int i = 0; i < isBold.length; i++) {
			sb.append(str.charAt(i));
			if (i == isBold.length - 1) {
				if (isBold[i]) {
					sb.append("</b>");
				}
				break;
			}

			if (!isBold[i] && isBold[i + 1]) {
				sb.append("<b>");
			}

			if (isBold[i] && !isBold[i + 1]) {
				sb.append("</b>");
			}
		}
		return sb.toString();
	}
}
