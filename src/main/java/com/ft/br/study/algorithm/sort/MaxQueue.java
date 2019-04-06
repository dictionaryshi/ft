package com.ft.br.study.algorithm.sort;

/**
 * 基于堆的优先队列
 *
 * @author shichunyang
 */
@SuppressWarnings("unchecked")
public class MaxQueue<T extends Comparable<T>> {
	private T[] queue;
	private int size = 0;

	public MaxQueue(int size) {
		queue = (T[]) new Comparable[size + 1];
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}

	public void insert(T value) {
		queue[++size] = value;
		Sort.swim(queue, size);
	}

	public T deleteMaxValue() {
		T maxValue = queue[1];
		Sort.exch(queue, 1, size--);
		queue[size + 1] = null;
		Sort.sink(queue, 1, size);
		return maxValue;
	}
}
