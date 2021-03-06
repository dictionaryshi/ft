package com.ft.br.study.algorithm.leetcode;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * 链表
 *
 * @author shichunyang
 */
@SuppressWarnings("unchecked")
public class Linked {

    @Data
    private static class Node {
        int value;
        Node next;

        public Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    /**
     * 删除链表倒数第n个元素
     */
    public Node removeNthFromEnd(Node head, int n) {
        Node dummy = new Node(0, null);
        dummy.next = head;
        Node first = dummy;
        Node second = dummy;
        for (int i = 1; i <= n + 1; i++) {
            first = first.next;
        }
        while (first != null) {
            first = first.next;
            second = second.next;
        }
        second.next = second.next.next;
        return dummy.next;
    }

    public Node reverseList(Node head) {
        Node prev = null;
        Node curr = head;
        while (curr != null) {
            Node nextTemp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextTemp;
        }
        return prev;
    }

    public Node mergeTwoLists2(Node l1, Node l2) {
        Node preHead = new Node(-1, null);

        Node prev = preHead;
        while (l1 != null && l2 != null) {
            if (l1.value <= l2.value) {
                prev.next = l1;
                l1 = l1.next;
            } else {
                prev.next = l2;
                l2 = l2.next;
            }
            prev = prev.next;
        }

        prev.next = l1 == null ? l2 : l1;

        return preHead.next;
    }

    public static void print(Node head) {
        while (head != null) {
            System.out.print(head.value + " ");
            head = head.next;
        }
        System.out.println();
    }

    public static int size(Node head) {
        if (head == null) {
            return 0;
        }
        int size = 0;
        Node current = head;
        while (current != null) {
            size++;
            current = current.next;
        }
        return size;
    }

    public boolean hasCycle(Node head) {
        Set<Node> nodesSeen = new HashSet<>();
        while (head != null) {
            if (nodesSeen.contains(head)) {
                return Boolean.TRUE;
            } else {
                nodesSeen.add(head);
            }
            head = head.next;
        }
        return Boolean.FALSE;
    }

    public boolean hasCycle2(Node head) {
        if (head == null || head.next == null) {
            return Boolean.FALSE;
        }
        Node slow = head;
        Node fast = head.next;
        while (slow != fast) {
            if (fast == null || fast.next == null) {
                return Boolean.FALSE;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        return Boolean.TRUE;
    }

    /**
     * 获取链表交点
     */
    public Node getIntersectionNode(Node headA, Node headB) {
        if (headA == null || headB == null) {
            return null;
        }
        Node pA = headA, pB = headB;
        while (pA != pB) {
            pA = pA == null ? headB : pA.next;
            pB = pB == null ? headA : pB.next;
        }
        return pA;
    }

    public static void main(String[] args) {
        Node node5 = new Node(1, null);
        Node node4 = new Node(3, node5);
        Node node3 = new Node(4, node4);
        Node node2 = new Node(2, node3);
        Node node1 = new Node(5, node2);
        Node head = new Node(0, node1);
    }
}
