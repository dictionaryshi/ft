package com.ft.study.source;

import java.util.*;
import java.util.function.Consumer;

/**
 * ArrayList
 *
 * @author shichunyang
 */
public class ArrayList<E> implements Iterable<E> {

	/**
	 * 数据结构改变的次数
	 */
	protected transient int modCount = 0;

	private static final int DEFAULT_CAPACITY = 10;

	/**
	 * 空数组对象
	 */
	private static final Object[] EMPTY_ELEMENTDATA = {};

	/**
	 * 默认空数组对象
	 */
	private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

	private Object[] elementData;

	private int size;

	public ArrayList(int initialCapacity) {

		if (initialCapacity > 0) {
			this.elementData = new Object[initialCapacity];
		} else if (initialCapacity == 0) {
			this.elementData = EMPTY_ELEMENTDATA;
		} else {
			throw new RuntimeException();
		}
	}

	public ArrayList() {
		this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
	}

	public ArrayList(Collection<? extends E> c) {
		elementData = c.toArray();
		if ((size = elementData.length) != 0) {

			// 如果数组不是Object[]类型, 那么将数组转成Object[]类型
			if (elementData.getClass() != Object[].class) {
				elementData = Arrays.copyOf(elementData, size, Object[].class);
			}
		} else {
			// 赋值为空数组
			this.elementData = EMPTY_ELEMENTDATA;
		}
	}

	/**
	 * 去掉数组中空闲的空间
	 */
	public void trimToSize() {
		// 数据结构改变次数+1
		modCount++;
		// 当元素个数小于数组长度时
		if (size < elementData.length) {
			elementData = (size == 0)
					? EMPTY_ELEMENTDATA
					: Arrays.copyOf(elementData, size);
		}
	}

	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

	/**
	 * 进行扩容, 最小长度是10
	 *
	 * @param minCapacity 要扩容的长度
	 */
	private void ensureCapacityInternal(int minCapacity) {
		// 如果是空数组的话, 那么扩容量最低是10
		if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
			minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
		}

		ensureExplicitCapacity(minCapacity);
	}

	/**
	 * 对扩容的长度进行校验, 扩容长度必须大于元素个数
	 *
	 * @param minCapacity 要扩容的长度
	 */
	private void ensureExplicitCapacity(int minCapacity) {
		modCount++;

		if (minCapacity - elementData.length > 0) {
			grow(minCapacity);
		}
	}

	/**
	 * 对数组进行扩容
	 *
	 * @param minCapacity 要扩容的长度
	 */
	private void grow(int minCapacity) {
		// 数组中元素的个数
		int oldCapacity = elementData.length;
		// 期望扩容长度(数组原始长度 + 数组原始长度 / 2)
		int newCapacity = oldCapacity + (oldCapacity >> 1);
		// 期望扩容长度如果小于指定扩容长度, 那么数组扩容长度为指定扩容长度
		if (newCapacity - minCapacity < 0) {
			newCapacity = minCapacity;
		}
		// 期望扩容长度, 不能超越数组长度边界值(Integer.MAX_VALUE)
		if (newCapacity - MAX_ARRAY_SIZE > 0) {
			newCapacity = hugeCapacity(minCapacity);
		}
		// 对数组进行扩容
		elementData = Arrays.copyOf(elementData, newCapacity);
	}

	/**
	 * 返回扩容长度的边界值, 扩容长度最大是Integer.MAX_VALUE。
	 *
	 * @param minCapacity 要扩容的长度
	 * @return 边界值
	 */
	private static int hugeCapacity(int minCapacity) {
		if (minCapacity < 0) {
			throw new OutOfMemoryError();
		}
		return (minCapacity > MAX_ARRAY_SIZE) ?
				Integer.MAX_VALUE :
				MAX_ARRAY_SIZE;
	}

	/**
	 * 返回数组中元素的个数
	 *
	 * @return 元素个数
	 */
	public int size() {
		return size;
	}

	/**
	 * 判断元素个数是否为0
	 *
	 * @return true:集合为空
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * 判断是否包含指定元素
	 *
	 * @param o 指定元素
	 * @return true:集合包含指定元素
	 */
	public boolean contains(Object o) {
		return indexOf(o) >= 0;
	}

	/**
	 * 从左到右查找元素
	 *
	 * @param o 指定元素
	 * @return 指定元素在数组中的索引
	 */
	public int indexOf(Object o) {
		if (o == null) {
			for (int i = 0; i < size; i++) {
				if (elementData[i] == null) {
					return i;
				}
			}
		} else {
			for (int i = 0; i < size; i++) {
				if (o.equals(elementData[i])) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * 从右到左查找元素
	 *
	 * @param o 指定元素
	 * @return 指定元素在数组中的索引
	 */
	public int lastIndexOf(Object o) {
		if (o == null) {
			for (int i = size - 1; i >= 0; i--) {
				if (elementData[i] == null) {
					return i;
				}
			}
		} else {
			for (int i = size - 1; i >= 0; i--) {
				if (o.equals(elementData[i])) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * 得到数组
	 *
	 * @return 数组
	 */
	public Object[] toArray() {
		return Arrays.copyOf(elementData, size);
	}

	/**
	 * 返回指定类型的数组
	 *
	 * @param a   指定的数组
	 * @param <T> 类型参数
	 * @return 指定类型的数组
	 */
	@SuppressWarnings("all")
	public <T> T[] toArray(T[] a) {
		if (a.length < size) {
			return (T[]) Arrays.copyOf(elementData, size, a.getClass());
		}
		System.arraycopy(elementData, 0, a, 0, size);
		if (a.length > size) {
			a[size] = null;
		}
		return a;
	}

	/**
	 * 根据索引获取元素
	 *
	 * @param index 数组的索引
	 * @return 元素值
	 */
	@SuppressWarnings("unchecked")
	E elementData(int index) {
		return (E) elementData[index];
	}

	/**
	 * 返回错误信息
	 *
	 * @param index 索引值
	 * @return 错误信息
	 */
	private String outOfBoundsMsg(int index) {
		return "index==>" + index + ", size==>" + size;
	}

	/**
	 * 校验索引
	 *
	 * @param index 索引
	 */
	private void rangeCheck(int index) {
		if (index >= size) {
			throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
		}
	}

	/**
	 * 校验添加索引(允许最大索引超一位)
	 *
	 * @param index 索引
	 */
	private void rangeCheckForAdd(int index) {
		if (index > size || index < 0) {
			throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
		}
	}

	/**
	 * 根据索引获取元素
	 *
	 * @param index 数组索引
	 * @return 元素值
	 */
	public E get(int index) {
		rangeCheck(index);

		return elementData(index);
	}

	/**
	 * 修改指定索引的元素, 并将老元素返回
	 *
	 * @param index   索引
	 * @param element 修改的新值
	 * @return 老元素值
	 */
	public E set(int index, E element) {
		rangeCheck(index);

		E oldValue = elementData(index);
		elementData[index] = element;
		return oldValue;
	}

	/**
	 * 添加元素, 并对数组进行扩容
	 *
	 * @param e 新添加的元素
	 * @return true:添加成功
	 */
	public boolean add(E e) {
		// 对数组进行扩容
		ensureCapacityInternal(size + 1);
		elementData[size++] = e;
		return true;
	}

	/**
	 * 在指定索引处添加元素
	 *
	 * @param index   索引
	 * @param element 新元素
	 */
	public void add(int index, E element) {
		rangeCheckForAdd(index);
		// 对数组进行扩容
		ensureCapacityInternal(size + 1);
		System.arraycopy(elementData, index, elementData, index + 1,
				size - index);
		elementData[index] = element;
		size++;
	}

	/**
	 * 根据索引删除元素
	 *
	 * @param index 数组索引
	 * @return 被删除的元素
	 */
	public E remove(int index) {
		rangeCheck(index);

		modCount++;
		E oldValue = elementData(index);

		// 需要移动的元素个数
		int numMoved = size - index - 1;
		if (numMoved > 0) {
			System.arraycopy(elementData, index + 1, elementData, index,
					numMoved);
		}
		// 让GC回收最后一个位置的元素对象
		elementData[--size] = null;

		return oldValue;
	}

	/**
	 * 根据数组索引删除元素
	 *
	 * @param index 数组索引
	 */
	private void fastRemove(int index) {
		modCount++;
		int numMoved = size - index - 1;
		if (numMoved > 0) {
			System.arraycopy(elementData, index + 1, elementData, index,
					numMoved);
		}
		elementData[--size] = null;
	}

	/**
	 * 在数组中删除指定对象(若对象存在多个, 只删除第一次出现的)
	 *
	 * @param o 指定对象
	 * @return true:删除成功
	 */
	public boolean remove(Object o) {
		if (o == null) {
			for (int index = 0; index < size; index++) {
				if (elementData[index] == null) {
					fastRemove(index);
					return true;
				}
			}
		} else {
			for (int index = 0; index < size; index++) {
				if (o.equals(elementData[index])) {
					fastRemove(index);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 清空数组, 并让GC回收数组元素对象
	 */
	public void clear() {
		modCount++;

		// clear to let GC do its work
		for (int i = 0; i < size; i++) {
			elementData[i] = null;
		}

		size = 0;
	}

	/**
	 * 批量添加元素到数组中
	 *
	 * @param c 要添加的元素集合
	 * @return true:添加成功
	 */
	public boolean addAll(Collection<? extends E> c) {
		Object[] a = c.toArray();
		int numNew = a.length;
		// 对数组进行扩容
		ensureCapacityInternal(size + numNew);
		System.arraycopy(a, 0, elementData, size, numNew);
		size += numNew;
		return numNew != 0;
	}

	/**
	 * 在指定索引处批量添加元素(索引值允许超出1个)
	 *
	 * @param index 指定的索引位置
	 * @param c     要添加的元素集合
	 * @return true:添加成功
	 */
	public boolean addAll(int index, Collection<? extends E> c) {
		rangeCheckForAdd(index);

		Object[] a = c.toArray();
		int numNew = a.length;
		// 对数组进行扩容
		ensureCapacityInternal(size + numNew);

		// 要移动的元素个数
		int numMoved = size - index;
		if (numMoved > 0) {
			System.arraycopy(elementData, index, elementData, index + numNew,
					numMoved);
		}
		System.arraycopy(a, 0, elementData, index, numNew);
		size += numNew;
		return numNew != 0;
	}

	@Override
	public Iterator<E> iterator() {
		return new Itr();
	}

	private class Itr implements Iterator<E> {
		/**
		 * 下一个元素的索引
		 */
		int cursor;
		/**
		 * 当前元素索引
		 */
		int lastRet = -1;
		int expectedModCount = modCount;

		@Override
		public boolean hasNext() {
			return cursor != size;
		}

		@SuppressWarnings("unchecked")
		@Override
		public E next() {
			checkForComodification();
			int i = cursor;
			if (i >= size) {
				throw new NoSuchElementException();
			}
			Object[] elementData = ArrayList.this.elementData;
			if (i >= elementData.length) {
				throw new ConcurrentModificationException();
			}
			// 下一个元素索引
			cursor = i + 1;
			// 当前元素索引
			return (E) elementData[lastRet = i];
		}

		@Override
		public void remove() {
			if (lastRet < 0) {
				throw new IllegalStateException();
			}
			checkForComodification();

			try {
				// 用当前元素索引删除当前元素
				ArrayList.this.remove(lastRet);
				// 删除后, 其它元素上前补位, 那么下一个元素索引不变, 继续遍历当前索引位置
				cursor = lastRet;
				// 重置当前索引
				lastRet = -1;
				expectedModCount = modCount;
			} catch (IndexOutOfBoundsException ex) {
				throw new ConcurrentModificationException();
			}
		}

		@Override
		@SuppressWarnings("unchecked")
		public void forEachRemaining(Consumer<? super E> consumer) {
			Objects.requireNonNull(consumer);
			final int size = ArrayList.this.size;
			int i = cursor;
			if (i >= size) {
				return;
			}
			final Object[] elementData = ArrayList.this.elementData;
			if (i >= elementData.length) {
				throw new ConcurrentModificationException();
			}
			while (i != size && modCount == expectedModCount) {
				consumer.accept((E) elementData[i++]);
			}
			cursor = i;
			lastRet = i - 1;
			checkForComodification();
		}

		final void checkForComodification() {
			if (modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}
		}
	}
}
