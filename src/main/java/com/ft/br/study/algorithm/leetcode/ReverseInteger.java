package com.ft.br.study.algorithm.leetcode;

/**
 * ReverseInteger
 *
 * @author shichunyang
 */
public class ReverseInteger {
	/**
	 * 反转数字
	 *
	 * @param x 原始数字
	 * @return 反转后的数字
	 */
	public static int reverse(int x) {
		int rev = 0;
		while (x != 0) {
			// 个位
			int pop = x % 10;
			// 十位
			x /= 10;

			if (rev > Integer.MAX_VALUE / 10 || (rev == Integer.MAX_VALUE / 10 && pop > 7)) {
				return 0;
			}
			if (rev < Integer.MIN_VALUE / 10 || (rev == Integer.MIN_VALUE / 10 && pop < -8)) {
				return 0;
			}

			rev = rev * 10 + pop;
		}
		return rev;
	}

	/**
	 * 字符串转数字
	 */
	public static int convert(String str) {
		if (str == null || str.length() == 0) {
			return 0;
		}
		str = str.trim();

		char firstChar = str.charAt(0);
		int sign = 1;
		int start = 0;
		long res = 0;
		if (firstChar == '+') {
			start++;
		} else if (firstChar == '-') {
			sign = -1;
			start++;
		}

		for (int i = start; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return (int) res * sign;
			}
			res = res * 10 + str.charAt(i) - '0';
			if (sign == 1 && res > Integer.MAX_VALUE) {
				return Integer.MAX_VALUE;
			}
			if (sign == -1 && res > Integer.MAX_VALUE) {
				return Integer.MIN_VALUE;
			}
		}
		return (int) res * sign;
	}

	public static void main(String[] args) {
		System.out.println(convert("257"));
		System.out.println(reverse(257));
	}
}
