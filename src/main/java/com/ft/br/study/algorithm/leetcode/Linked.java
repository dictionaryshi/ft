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

	public static Node reverse1(Node head) {
		if (head == null || head.next == null) {
			return head;
		}

		Node reverseNode = null;
		while (head != null) {
			Node tempNode = head;
			head = head.next;
			tempNode.next = reverseNode;
			reverseNode = tempNode;
		}

		return reverseNode;
	}

	public static Node reverse2(Node head) {
		if (head == null || head.next == null) {
			return head;
		}

		Node reverseNode = reverse2(head.next);
		head.next.next = head;
		head.next = null;
		return reverseNode;
	}

	/**
	 * 查找倒数第k个元素
	 */
	public static Node find(Node head, int k) {
		if (k < 1 || head == null) {
			return null;
		}

		Node fast = head;
		Node slow = head;

		for (int i = 0; i < k; i++) {
			if (fast == null) {
				return null;
			}
			fast = fast.next;
		}

		while (fast != null) {
			fast = fast.next;
			slow = slow.next;
		}

		return slow;
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
