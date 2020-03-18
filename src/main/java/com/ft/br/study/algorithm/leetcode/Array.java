package com.ft.br.study.algorithm.leetcode;

import java.util.Arrays;

/**
 * 数组类题目
 *
 * @author shichunyang
 */
public class Array {

	/**
	 * 删除排序数组中的重复项(重复元素放在数组末尾)
	 *
	 * @return 不重复的元素个数
	 */
	public int removeDuplicates(int[] nums) {
		if (nums.length == 0) {
			return 0;
		}

		int i = 0;
		for (int j = 1; j < nums.length; j++) {
			if (nums[j] != nums[i]) {
				i++;
				nums[i] = nums[j];
			}
		}
		return i + 1;
	}

	/**
	 * 删除数组中指定元素
	 *
	 * @return 剩余元素个数
	 */
	public int removeElement(int[] nums, int val) {
		int i = 0;
		for (int j = 0; j < nums.length; j++) {
			if (nums[j] != val) {
				nums[i] = nums[j];
				i++;
			}
		}
		return i;
	}

	/**
	 * 查找两个有序数组的中位
	 */
	public double findMedianSortedArrays(int[] a, int[] b) {
		int m = a.length;
		int n = b.length;
		int len = m + n;
		int left = -1, right = -1;
		int aStart = 0, bStart = 0;
		for (int i = 0; i <= len / 2; i++) {
			left = right;
			if (aStart < m && (bStart >= n || a[aStart] < b[bStart])) {
				// a没走完 && (b走完了 || a < b)
				right = a[aStart++];
			} else {
				right = b[bStart++];
			}
		}
		if ((len & 1) == 0) {
			return (left + right) / 2.0;
		} else {
			return right;
		}
	}

	/**
	 * 数组加一
	 */
	public int[] plusOne(int[] digits) {
		for (int i = digits.length - 1; i >= 0; i--) {
			digits[i]++;
			digits[i] = digits[i] % 10;
			if (digits[i] != 0) {
				return digits;
			}
		}
		digits = new int[digits.length + 1];
		digits[0] = 1;
		return digits;
	}

	/**
	 * 合并两个有序数组
	 * 输入:
	 * nums1 = [1,2,3,0,0,0], m = 3
	 * nums2 = [2,5,6],       n = 3
	 * 输出: [1,2,2,3,5,6]
	 */
	public void merge(int[] nums1, int m, int[] nums2, int n) {
		int len1 = m - 1;
		int len2 = n - 1;
		int len = m + n - 1;
		while (len1 >= 0 && len2 >= 0) {
			nums1[len--] = nums1[len1] > nums2[len2] ? nums1[len1--] : nums2[len2--];
		}
		System.arraycopy(nums2, 0, nums1, 0, len2 + 1);
	}

	/**
	 * 买股票最大利润
	 * 输入: [7,1,5,3,6,4]
	 * 输出: 5
	 */
	public int maxProfit(int[] prices) {
		int minPrice = Integer.MAX_VALUE;
		int maxProfit = 0;
		for (int i = 0; i < prices.length; i++) {
			if (prices[i] < minPrice) {
				minPrice = prices[i];
			} else if (prices[i] - minPrice > maxProfit) {
				maxProfit = prices[i] - minPrice;
			}
		}
		return maxProfit;
	}

	/**
	 * 给定n个整数, 找出平均数最大且长度为k的连续子数组, 并输出该最大平均数。
	 */
	public double findMaxAverage(int[] nums, int k) {
		double sum = 0;
		for (int i = 0; i < k; i++) {
			sum += nums[i];
		}
		double res = sum;
		for (int i = k; i < nums.length; i++) {
			sum += nums[i] - nums[i - k];
			res = Math.max(res, sum);
		}
		return res / k;
	}

	/**
	 * 打家劫舍
	 */
	public int rob(int[] nums) {
		if (nums.length == 0) {
			return 0;
		} else if (nums.length == 1) {
			return nums[0];
		}
		int[] ans = new int[nums.length];
		ans[0] = nums[0];
		ans[1] = Math.max(nums[0], nums[1]);
		for (int i = 2; i < nums.length; i++) {
			ans[i] = Math.max(nums[i] + ans[i - 2], ans[i - 1]);
		}
		return ans[ans.length - 1];
	}

	/**
	 * 分饼干(贪心算法)
	 */
	public int findContentChildren(int[] q, int[] s) {
		Arrays.sort(q);
		Arrays.sort(s);
		int i = 0;
		for (int j = 0; i < q.length && j < s.length; j++) {
			if (s[j] >= q[i]) {
				i++;
			}
		}
		return i;
	}
}
