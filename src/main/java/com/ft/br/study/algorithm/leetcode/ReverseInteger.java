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
			rev = rev * 10 + pop;
		}
		return rev;
	}

	public static int convert(String str) {
		int result = 0;
		char[] chars = str.toCharArray();
		for (char ch : chars) {
			result = result * 10 + (ch - '0');
		}
		return result;
	}

	public boolean isPalindrome(int x) {
		if (x < 0) {
			return false;
		}
		int res = 0;
		int y = x;
		while (y != 0) {
			res = res * 10 + y % 10;
			y /= 10;
		}
		return res == x;
	}

	public static void main(String[] args) {
		System.out.println(convert("257"));
		System.out.println(reverse(257));
	}
}
