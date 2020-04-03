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

	/**
	 * 字符串相加
	 */
	public String addStrings(String num1, String num2) {
		StringBuilder res = new StringBuilder("");
		int i = num1.length() - 1, j = num2.length() - 1, carry = 0;
		while (i >= 0 || j >= 0) {
			int n1 = i >= 0 ? num1.charAt(i) - '0' : 0;
			int n2 = j >= 0 ? num2.charAt(j) - '0' : 0;
			int tmp = n1 + n2 + carry;
			carry = tmp / 10;
			res.append(tmp % 10);
			i--;
			j--;
		}
		if (carry == 1) {
			res.append(1);
		}
		return res.reverse().toString();
	}
}
