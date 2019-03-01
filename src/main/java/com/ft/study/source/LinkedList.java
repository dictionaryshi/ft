package com.ft.study.source;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * LinkedList 源码
 *
 * @author shichunyang
 */
public class LinkedList<E> {

	private transient int size = 0;

	private transient int modCount = 0;

	/**
	 * 指向第一个结点(first == null && last == null) ||
	 * (first.prev == null && first.item != null)
	 */
	private transient Node<E> first;
	/**
	 * 指向最后一个结点(first == null && last == null) ||
	 * (last.next == null && last.item != null)
	 */
	private transient Node<E> last;

	/**
	 * 链表类
	 *
	 * @param <E> 参数类型
	 */
	private static class Node<E> {
		E item;
		Node<E> next;
		Node<E> prev;

		Node(Node<E> prev, E element, Node<E> next) {
			this.item = element;
			this.next = next;
			this.prev = prev;
		}
	}

	/**
	 * 创建一个空链表
	 */
	public LinkedList() {
	}

	/**
	 * 查询链表中指定索引的元素(一个一个的遍历)
	 *
	 * @param index 指定索引
	 * @return 元素对象
	 */
	Node<E> node(int index) {
		if (index < (size >> 1)) {
			Node<E> x = first;
			for (int i = 0; i < index; i++) {
				x = x.next;
			}
			return x;
		} else {
			Node<E> x = last;
			for (int i = size - 1; i > index; i--) {
				x = x.prev;
			}
			return x;
		}
	}

	/**
	 * 判断指定索引是否是合法的
	 *
	 * @param index 指定索引
	 * @return true:索引是合法的
	 */
	private boolean isElementIndex(int index) {
		return index >= 0 && index < size;
	}

	/**
	 * 判断索引是否是允许超出一位的合法索引
	 *
	 * @param index 指定索引
	 * @return true:索引合法
	 */
	private boolean isPositionIndex(int index) {
		return index >= 0 && index <= size;
	}

	/**
	 * 打印错误信息
	 *
	 * @param index 索引
	 * @return 错误信息
	 */
	private String outOfBoundsMsg(int index) {
		return "index==>" + index + ", size==>" + size;
	}

	/**
	 * 检查是否是合法索引
	 *
	 * @param index 指定索引
	 */
	private void checkElementIndex(int index) {
		if (!isElementIndex(index)) {
			throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
		}
	}

	/**
	 * 检查是否是允许超出一位的合法索引
	 *
	 * @param index 指定索引
	 */
	private void checkPositionIndex(int index) {
		if (!isPositionIndex(index)) {
			throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
		}
	}

	/**
	 * 在指定索引处, 批量添加元素
	 *
	 * @param index 指定索引
	 * @param c     批量元素
	 * @return true:添加成功
	 */
	public boolean addAll(int index, Collection<? extends E> c) {
		// 检查索引合法性(允许超一位)
		checkPositionIndex(index);

		Object[] a = c.toArray();
		int numNew = a.length;
		if (numNew == 0) {
			return false;
		}

		// succ保存的是index索引的元素
		Node<E> pred, succ;
		if (index == size) {
			// index为超一位索引, 那么当前位置元素为null
			succ = null;
			pred = last;
		} else {
			// 查找index索引对象
			succ = node(index);
			pred = succ.prev;
		}

		for (Object o : a) {
			@SuppressWarnings("unchecked") E e = (E) o;
			// 创建新的结点对象, 并指定新结点的前一个结点对象
			Node<E> newNode = new Node<>(pred, e, null);
			if (pred == null) {
				first = newNode;
			} else {
				// 为上一个结点, 指定后一个结点对象(新创建的结点对象)
				pred.next = newNode;
			}
			// pred从此指向新结点对象
			pred = newNode;
		}

		if (succ == null) {
			// index为超一位索引, 那么最后一个结点应该是最后一个新结点对象
			last = pred;
		} else {
			// 为最后一个新结点对象指定下一个结点对象(index索引对象)
			pred.next = succ;
			// 为index索引对象指定上一个结点对象(最后一个新结点对象)
			succ.prev = pred;
		}

		size += numNew;
		modCount++;
		return true;
	}

	/**
	 * 批量添加结点对象
	 *
	 * @param c 批量对象
	 * @return true:添加成功
	 */
	public boolean addAll(Collection<? extends E> c) {
		return addAll(size, c);
	}

	/**
	 * 创建链表的同时添加元素
	 *
	 * @param c 批量元素
	 */
	public LinkedList(Collection<? extends E> c) {
		this();
		addAll(c);
	}

	/**
	 * 在链表头部插入元素
	 *
	 * @param e 新元素
	 */
	private void linkFirst(E e) {
		final Node<E> f = first;
		final Node<E> newNode = new Node<>(null, e, f);
		first = newNode;
		if (f == null) {
			// 如果原来没有头元素, 那么新增元素也是尾元素
			last = newNode;
		} else {
			// 为老元素指定上一个元素(新增元素)
			f.prev = newNode;
		}
		size++;
		modCount++;
	}

	/**
	 * 在链表尾部添加元素
	 *
	 * @param e 新元素
	 */
	void linkLast(E e) {
		final Node<E> l = last;
		final Node<E> newNode = new Node<>(l, e, null);
		last = newNode;
		if (l == null) {
			// 如果原来没有尾元素, 那么新增元素也是头元素
			first = newNode;
		} else {
			// 为老元素指定下一个元素(新增元素)
			l.next = newNode;
		}
		size++;
		modCount++;
	}

	/**
	 * 在非空元素前添加新元素
	 *
	 * @param e    新元素
	 * @param succ 非空元素
	 */
	void linkBefore(E e, Node<E> succ) {
		// 非空元素的前一个元素
		final Node<E> pred = succ.prev;
		final Node<E> newNode = new Node<>(pred, e, succ);
		// 非空元素的前一个元素需要指向新元素
		succ.prev = newNode;
		if (pred == null) {
			first = newNode;
		} else {
			pred.next = newNode;
		}
		size++;
		modCount++;
	}

	/**
	 * 删除头元素
	 *
	 * @param f 头元素
	 * @return 删除的元素值
	 */
	private E unlinkFirst(Node<E> f) {
		// assert f == first && f != null;

		// 头元素值
		final E element = f.item;
		// 头元素的下一个元素
		final Node<E> next = f.next;
		// help GC
		f.item = null;
		// help GC
		f.next = null;

		first = next;
		if (next == null) {
			last = null;
		} else {
			next.prev = null;
		}
		size--;
		modCount++;
		return element;
	}

	/**
	 * 删除尾元素
	 *
	 * @param l 尾元素
	 * @return 删除的元素值
	 */
	private E unlinkLast(Node<E> l) {
		// assert l == last && l != null;
		final E element = l.item;
		final Node<E> prev = l.prev;
		// help GC
		l.item = null;
		// help GC
		l.prev = null;

		last = prev;

		if (prev == null) {
			first = null;
		} else {
			prev.next = null;
		}
		size--;
		modCount++;
		return element;
	}

	/**
	 * 删除非空元素
	 *
	 * @param x 非空元素
	 * @return 被删除的元素
	 */
	E unlink(Node<E> x) {
		// assert x != null;
		final E element = x.item;
		final Node<E> next = x.next;
		final Node<E> prev = x.prev;

		if (prev == null) {
			// x是头结点, 那么头结点变为x的下一个结点
			first = next;
		} else {
			prev.next = next;
			x.prev = null;
		}

		if (next == null) {
			// x是尾结点, 那么尾结点变为x的上一个结点
			last = prev;
		} else {
			next.prev = prev;
			x.next = null;
		}

		x.item = null;
		size--;
		modCount++;
		return element;
	}

	/**
	 * 获取头元素
	 *
	 * @return 头元素
	 */
	public E getFirst() {
		final Node<E> f = first;
		if (f == null) {
			throw new NoSuchElementException();
		}
		return f.item;
	}

	/**
	 * 获取尾元素
	 *
	 * @return 尾元素
	 */
	public E getLast() {
		final Node<E> l = last;
		if (l == null) {
			throw new NoSuchElementException();
		}
		return l.item;
	}

	/**
	 * 删除头元素
	 *
	 * @return 头元素
	 */
	public E removeFirst() {
		final Node<E> f = first;
		if (f == null) {
			throw new NoSuchElementException();
		}
		return unlinkFirst(f);
	}

	/**
	 * 删除尾元素
	 *
	 * @return 尾元素
	 */
	public E removeLast() {
		final Node<E> l = last;
		if (l == null) {
			throw new NoSuchElementException();
		}
		return unlinkLast(l);
	}

	/**
	 * 在头部添加元素
	 *
	 * @param e 新的元素
	 */
	public void addFirst(E e) {
		linkFirst(e);
	}

	/**
	 * 在尾部添加元素
	 *
	 * @param e 新元素
	 */
	public void addLast(E e) {
		linkLast(e);
	}

	/**
	 * 返回元素个数
	 *
	 * @return 元素个数
	 */
	public int size() {
		return size;
	}

	/**
	 * 在尾部添加元素
	 *
	 * @param e 新元素
	 * @return true:添加成功
	 */
	public boolean add(E e) {
		linkLast(e);
		return true;
	}

	/**
	 * 遍历查找元素的索引
	 *
	 * @param o 要查找的元素
	 * @return 元素索引
	 */
	public int indexOf(Object o) {
		int index = 0;
		if (o == null) {
			for (Node<E> x = first; x != null; x = x.next) {
				if (x.item == null) {
					return index;
				}
				index++;
			}
		} else {
			for (Node<E> x = first; x != null; x = x.next) {
				if (o.equals(x.item)) {
					return index;
				}
				index++;
			}
		}
		return -1;
	}

	/**
	 * 判断链表是否包含某个元素
	 *
	 * @param o 指定元素
	 * @return true:包含
	 */
	public boolean contains(Object o) {
		return indexOf(o) != -1;
	}

	/**
	 * 删除对象元素
	 *
	 * @param o 对象元素
	 * @return true:删除成功
	 */
	public boolean remove(Object o) {
		if (o == null) {
			for (Node<E> x = first; x != null; x = x.next) {
				if (x.item == null) {
					unlink(x);
					return true;
				}
			}
		} else {
			for (Node<E> x = first; x != null; x = x.next) {
				if (o.equals(x.item)) {
					unlink(x);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 清空链表
	 */
	public void clear() {
		for (Node<E> x = first; x != null; ) {
			Node<E> next = x.next;
			x.item = null;
			x.next = null;
			x.prev = null;
			x = next;
		}
		first = last = null;
		size = 0;
		modCount++;
	}

	/**
	 * 根据索引获取元素值
	 *
	 * @param index 索引
	 * @return 元素值
	 */
	public E get(int index) {
		checkElementIndex(index);
		return node(index).item;
	}

	/**
	 * 根据索引修改元素值
	 *
	 * @param index   索引
	 * @param element 新的元素值
	 * @return 老值
	 */
	public E set(int index, E element) {
		checkElementIndex(index);
		Node<E> x = node(index);
		E oldVal = x.item;
		x.item = element;
		return oldVal;
	}

	/**
	 * 在某个索引处添加新元素
	 *
	 * @param index   索引
	 * @param element 新元素
	 */
	public void add(int index, E element) {
		checkPositionIndex(index);

		if (index == size) {
			linkLast(element);
		} else {
			linkBefore(element, node(index));
		}
	}

	/**
	 * 删除指定索引元素
	 *
	 * @param index 索引
	 * @return 被删除的元素
	 */
	public E remove(int index) {
		checkElementIndex(index);
		return unlink(node(index));
	}

	/**
	 * 从后向前遍历元素
	 *
	 * @param o 指定元素
	 * @return 索引
	 */
	public int lastIndexOf(Object o) {
		int index = size;
		if (o == null) {
			for (Node<E> x = last; x != null; x = x.prev) {
				index--;
				if (x.item == null) {
					return index;
				}
			}
		} else {
			for (Node<E> x = last; x != null; x = x.prev) {
				index--;
				if (o.equals(x.item)) {
					return index;
				}
			}
		}
		return -1;
	}

	/**
	 * 链表转换成数组
	 *
	 * @return 数组
	 */
	public Object[] toArray() {
		Object[] result = new Object[size];
		int i = 0;
		for (Node<E> x = first; x != null; x = x.next) {
			result[i++] = x.item;
		}
		return result;
	}

	/**
	 * 将链表转换成指定类型的数组
	 *
	 * @param a   指定类型的数组
	 * @param <T> 类型参数
	 * @return 数组
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		if (a.length < size) {
			a = (T[]) java.lang.reflect.Array.newInstance(
					a.getClass().getComponentType(), size);
		}
		int i = 0;
		Object[] result = a;
		for (Node<E> x = first; x != null; x = x.next) {
			result[i++] = x.item;
		}

		if (a.length > size) {
			a[size] = null;
		}

		return a;
	}

}
