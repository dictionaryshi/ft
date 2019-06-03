package com.ft.br.study.algorithm.leetcode;

import com.ft.util.JsonUtil;
import lombok.Data;

/**
 * 两数相加
 *
 * @author shichunyang
 */
public class AddTwoNumber {
	@Data
	static class ListNode {
		int val;
		ListNode next;

		ListNode(int val) {
			this.val = val;
		}
	}

	public static ListNode addTwoNumber(ListNode l1, ListNode l2) {
		ListNode res = new ListNode(-1);
		// 当前操作节点
		ListNode cur = res;
		// 商
		int quotient = 0;
		while (l1 != null || l2 != null || quotient != 0) {
			int t = (l1 == null ? 0 : l1.val) + (l2 == null ? 0 : l2.val) + quotient;
			quotient = t / 10;
			// 余数
			ListNode node = new ListNode(t % 10);
			cur.next = node;
			cur = node;
			l1 = (l1 == null) ? null : l1.next;
			l2 = (l2 == null) ? null : l2.next;
		}
		return res.next;
	}

	public static void main(String[] args) {
		ListNode two = new ListNode(2);
		ListNode four = new ListNode(4);
		ListNode three = new ListNode(3);
		two.next = four;
		four.next = three;

		ListNode five = new ListNode(5);
		ListNode six = new ListNode(6);
		ListNode seven = new ListNode(7);

		five.next = six;
		six.next = seven;

		System.out.println(JsonUtil.object2Json(two));
		System.out.println(JsonUtil.object2Json(five));
		System.out.println(JsonUtil.object2Json(addTwoNumber(two, five)));
	}
}
