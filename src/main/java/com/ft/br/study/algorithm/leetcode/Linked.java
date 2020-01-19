package com.ft.br.study.algorithm.leetcode;

/**
 * 链表
 *
 * @author shichunyang
 */
@SuppressWarnings("unchecked")
public class Linked {

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

	public Node reverseList2(Node head) {
		if (head == null || head.next == null) {
			return head;
		}
		Node p = reverseList2(head.next);
		head.next.next = head;
		head.next = null;
		return p;
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

	public static Node merge(Node head1, Node head2) {
		if (head1 == null) {
			return head2;
		}
		if (head2 == null) {
			return head1;
		}

		Node mergeHead;
		if (head1.value < head2.value) {
			mergeHead = head1;
			mergeHead.next = merge(head1.next, head2);
		} else {
			mergeHead = head2;
			mergeHead.next = merge(head1, head2.next);
		}
		return mergeHead;
	}

	public static boolean hasCycle(Node head) {
		Node fast = head;
		Node slow = head;

		while (fast != null && fast.next != null) {
			fast = fast.next.next;
			slow = slow.next;
			if (fast == slow) {
				return true;
			}
		}
		return false;
	}

	public static Node getFirstNodeInCycle(Node head) {
		Node slow = head;
		Node fast = head;

		// 找到快慢指针相遇点
		while (fast != null && fast.next != null) {
			slow = slow.next;
			fast = fast.next.next;
			if (slow == fast) {
				break;
			}
		}

		// 没有环的情况
		if (fast == null || fast.next == null) {
			return null;
		}

		slow = head;
		while (slow != fast) {
			slow = slow.next;
			fast = fast.next;
		}

		return fast;
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
