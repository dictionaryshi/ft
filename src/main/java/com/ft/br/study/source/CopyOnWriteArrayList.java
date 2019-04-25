package com.ft.br.study.source;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 读写分离list
 *
 * @author shichunyang
 */
@SuppressWarnings("unchecked")
public class CopyOnWriteArrayList<E> {
	final transient ReentrantLock lock = new ReentrantLock();

	private transient volatile Object[] array;

	final Object[] getArray() {
		return array;
	}

	final void setArray(Object[] a) {
		array = a;
	}

	public CopyOnWriteArrayList() {
		setArray(new Object[0]);
	}

	public CopyOnWriteArrayList(E[] toCopyIn) {
		setArray(Arrays.copyOf(toCopyIn, toCopyIn.length, Object[].class));
	}

	public int size() {
		return getArray().length;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public Object[] toArray() {
		Object[] elements = getArray();
		return Arrays.copyOf(elements, elements.length);
	}

	private E get(Object[] a, int index) {
		return (E) a[index];
	}

	public E get(int index) {
		return get(getArray(), index);
	}

	public E set(int index, E element) {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			Object[] elements = getArray();
			E oldValue = get(elements, index);

			if (oldValue != element) {
				int len = elements.length;
				Object[] newElements = Arrays.copyOf(elements, len);
				newElements[index] = element;
				setArray(newElements);
			} else {
				setArray(elements);
			}
			return oldValue;
		} finally {
			lock.unlock();
		}
	}

	public boolean add(E e) {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			Object[] elements = getArray();
			int len = elements.length;
			Object[] newElements = Arrays.copyOf(elements, len + 1);
			newElements[len] = e;
			setArray(newElements);
			return true;
		} finally {
			lock.unlock();
		}
	}

	public void add(int index, E element) {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			Object[] elements = getArray();
			int len = elements.length;
			if (index > len || index < 0) {
				throw new IndexOutOfBoundsException("Index: " + index +
						", Size: " + len);
			}
			Object[] newElements;
			int numMoved = len - index;
			if (numMoved == 0) {
				newElements = Arrays.copyOf(elements, len + 1);
			} else {
				newElements = new Object[len + 1];
				System.arraycopy(elements, 0, newElements, 0, index);
				System.arraycopy(elements, index, newElements, index + 1,
						numMoved);
			}
			newElements[index] = element;
			setArray(newElements);
		} finally {
			lock.unlock();
		}
	}

	public E remove(int index) {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			Object[] elements = getArray();
			int len = elements.length;
			E oldValue = get(elements, index);
			int numMoved = len - index - 1;
			if (numMoved == 0) {
				setArray(Arrays.copyOf(elements, len - 1));
			} else {
				Object[] newElements = new Object[len - 1];
				System.arraycopy(elements, 0, newElements, 0, index);
				System.arraycopy(elements, index + 1, newElements, index,
						numMoved);
				setArray(newElements);
			}
			return oldValue;
		} finally {
			lock.unlock();
		}
	}
}
