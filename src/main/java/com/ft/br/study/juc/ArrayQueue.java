package com.ft.br.study.juc;

/**
 * 数组实现阻塞队列
 *
 * @author shichunyang
 */
@SuppressWarnings("unchecked")
public final class ArrayQueue<T> {

	/**
	 * 队列数量
	 */
	private int count = 0;

	/**
	 * 最终的数据存储
	 */
	private Object[] array;

	/**
	 * 队列满时的阻塞锁
	 */
	private final Object full = new Object();

	/**
	 * 队列空时的阻塞锁
	 */
	private final Object empty = new Object();


	/**
	 * 写入数据时的下标
	 */
	private int putIndex;

	/**
	 * 获取数据时的下标
	 */
	private int getIndex;

	public ArrayQueue(int size) {
		array = new Object[size];
	}

	/**
	 * 从队列尾写入数据
	 */
	public void put(T t) {
		synchronized (full) {
			while (count == array.length) {
				try {
					full.wait();
				} catch (Exception e) {
					break;
				}
			}
		}

		synchronized (empty) {
			// 写入
			array[putIndex] = t;
			count++;

			putIndex++;
			if (putIndex == array.length) {
				// 超过数组长度后需要从头开始
				putIndex = 0;
			}

			empty.notify();
		}

	}

	/**
	 * 从队列头获取数据
	 */
	public T get() {
		synchronized (empty) {
			while (count == 0) {
				try {
					empty.wait();
				} catch (Exception e) {
					return null;
				}
			}
		}

		synchronized (full) {
			Object result = array[getIndex];
			array[getIndex] = null;
			count--;

			getIndex++;
			if (getIndex == array.length) {
				getIndex = 0;
			}

			full.notify();

			return (T) result;
		}
	}

	/**
	 * 获取队列大小
	 */
	public synchronized int size() {
		return count;
	}


	/**
	 * 判断队列是否为空
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
}
