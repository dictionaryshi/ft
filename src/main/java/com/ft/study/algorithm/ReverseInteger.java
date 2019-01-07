package com.ft.study.algorithm;

/**
 * ReverseInteger
 *
 * @author shichunyang
 */
public class ReverseInteger {
	/**
	 * 反转数字
	 *
	 * @param number 原始数字
	 * @return 反转后的数字
	 */
	public static int reverse(int number) {
		long result = 0;

		for (; number != 0; number = number / 10) {
			result = result * 10 + number % 10;
		}

		if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
			return 0;
		}

		return (int) result;
	}

	public static int convert(String str) {
		int result = 0;
		char[] chars = str.toCharArray();
		for (char ch : chars) {
			result = result * 10 + (ch - '0');
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println(convert("257"));
		System.out.println(reverse(257));
	}
}
