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

    /**
     * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
     * 输出：7 -> 0 -> 8
     * 原因：342 + 465 = 807
     */
    public static ListNode addTwoNumber(ListNode l1, ListNode l2) {
        ListNode head = new ListNode(-1);
        ListNode p = l1, q = l2, curr = head;
        int carry = 0;
        while (p != null || q != null) {
            int x = (p != null) ? p.val : 0;
            int y = (q != null) ? q.val : 0;
            int sum = carry + x + y;
            // 十位
            carry = sum / 10;
            // 将个位存起来
            curr.next = new ListNode(sum % 10);
            curr = curr.next;
            if (p != null) {
                p = p.next;
            }
            if (q != null) {
                q = q.next;
            }
        }
        if (carry > 0) {
            curr.next = new ListNode(carry);
        }
        return head.next;
    }

    public static void main(String[] args) {
        ListNode two = new ListNode(2);
        ListNode four = new ListNode(4);
        two.next = four;
        four.next = new ListNode(3);

        ListNode five = new ListNode(5);
        ListNode six = new ListNode(6);
        five.next = six;
        six.next = new ListNode(4);

        System.out.println(JsonUtil.object2Json(two));
        System.out.println(JsonUtil.object2Json(five));
        System.out.println(JsonUtil.object2Json(addTwoNumber(two, five)));
    }
}
