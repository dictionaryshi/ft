package com.ft.br.study.algorithm.leetcode;

import java.util.HashMap;

/**
 * LruCache
 *
 * @author shichunyang
 */
public class LruCache {

	private class Node {

		private int key, val;

		public Node next, prev;

		private Node(int k, int v) {
			this.key = k;
			this.val = v;
		}
	}

	private class DoubleList {

		/**
		 * 头尾节点
		 */
		private Node head, tail;

		/**
		 * 链表元素数
		 */
		private int size;

		public DoubleList() {
			head = new Node(0, 0);
			tail = new Node(0, 0);
			head.next = tail;
			tail.prev = head;
			size = 0;
		}

		/**
		 * 在链表头部添加节点(head之后)
		 */
		public void addFirst(Node x) {
			x.next = head.next;
			x.prev = head;
			head.next.prev = x;
			head.next = x;
			size++;
		}

		/**
		 * 删除链表中的x节点(x一定存在)
		 */
		public void remove(Node x) {
			x.prev.next = x.next;
			x.next.prev = x.prev;
			size--;
		}

		/**
		 * 删除链表中最后一个节点, 并返回该节点
		 */
		public Node removeLast() {
			if (tail.prev == head) {
				return null;
			}

			Node last = tail.prev;
			remove(last);
			return last;
		}

		/**
		 * 返回链表长度
		 */
		public int size() {
			return size;
		}
	}

	private HashMap<Integer, Node> map;

	private DoubleList cache;

	/**
	 * 最大容量
	 */
	private int cap;

	public LruCache(int capacity) {
		this.cap = capacity;
		map = new HashMap<>();
		cache = new DoubleList();
	}

	public int get(int key) {
		if (!map.containsKey(key)) {
			return -1;
		}

		int val = map.get(key).val;
		// 把数据提到头部
		put(key, val);
		return val;
	}

	public void put(int key, int val) {
		Node x = new Node(key, val);

		if (map.containsKey(key)) {
			// 删除旧的节点, 新的插到头部
			cache.remove(map.get(key));
			cache.addFirst(x);
			// 更新map中对应的数据
			map.put(key, x);
		} else {
			if (cap == cache.size()) {
				// 删除链表最后一个数据
				Node last = cache.removeLast();
				map.remove(last.key);
			}
			// 直接添加到头部
			cache.addFirst(x);
			map.put(key, x);
		}
	}
}
