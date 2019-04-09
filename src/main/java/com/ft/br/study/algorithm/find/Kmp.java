package com.ft.br.study.algorithm.find;

public class Kmp {
	private static int partMatchValue(String subStr) {
		int length = subStr.length();
		String prefixStr = subStr.substring(0, length - 1);
		String suffixStr = subStr.substring(1);

		while (prefixStr.length() > 0 && suffixStr.length() > 0) {
			if (prefixStr.equals(suffixStr)) {
				return prefixStr.length();
			}

			if (prefixStr.length() == 1 && suffixStr.length() == 1) {
				break;
			}
			prefixStr = prefixStr.substring(0, prefixStr.length() - 1);
			suffixStr = suffixStr.substring(1, suffixStr.length());
		}

		return 0;
	}

	private static int[] partMatchTable(String pattern) {
		int patternLength = pattern.length();
		int[] matchTable = new int[patternLength];

		for (int i = 0; i < patternLength; i++) {
			int matchValue;
			if (i == 0) {
				matchValue = 0;
			} else {
				matchValue = partMatchValue(pattern.substring(0, i + 1));
			}

			matchTable[i] = matchValue;
		}

		return matchTable;
	}

	private static int kmp(String target, String pattern) {
		int[] partMatchTable = partMatchTable(pattern);

		char[] targetCharArr = target.toCharArray();
		char[] patternCharArr = pattern.toCharArray();

		int i = 0, j = 0;
		while (i < targetCharArr.length) {
			if (targetCharArr[i] == patternCharArr[j]) {
				i++;
				j++;
			} else if (j == 0) {
				i++;
			} else {
				// 部分匹配数
				j = partMatchTable[j - 1];
			}

			if (j == patternCharArr.length) {
				return i - patternCharArr.length;
			}

		}
		return -1;

	}

	public static void main(String[] args) {
		System.out.println(kmp("ABCABABCAD", "ABCAD"));
	}
}
