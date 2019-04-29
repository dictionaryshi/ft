package com.ft.br.study.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ArrayBlockingQueue
 *
 * @author shichunyang
 */
@SuppressWarnings("unchecked")
public class ArrayBlockingQueue<E> {
	final Object[] items;
	int takeIndex;
	int putIndex;
	int count;

	final ReentrantLock lock;
	private final Condition notEmpty;
	private final Condition notFull;

	public ArrayBlockingQueue(int capacity) {
		this(capacity, false);
	}

	public ArrayBlockingQueue(int capacity, boolean fair) {
		if (capacity <= 0) {
			throw new IllegalArgumentException();
		}
		this.items = new Object[capacity];
		lock = new ReentrantLock(fair);
		notEmpty = lock.newCondition();
		notFull = lock.newCondition();
	}

	public boolean offer(E e) {
		checkNotNull(e);
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			if (count == items.length) {
				return false;
			} else {
				enqueue(e);
				return true;
			}
		} finally {
			lock.unlock();
		}
	}

	public boolean offer(E e, long timeout, TimeUnit unit)
			throws InterruptedException {

		checkNotNull(e);
		long nanos = unit.toNanos(timeout);
		final ReentrantLock lock = this.lock;
		lock.lockInterruptibly();
		try {
			while (count == items.length) {
				if (nanos <= 0) {
					return false;
				}
				nanos = notFull.awaitNanos(nanos);
			}
			enqueue(e);
			return true;
		} finally {
			lock.unlock();
		}
	}

	private static void checkNotNull(Object v) {
		if (v == null) {
			throw new NullPointerException();
		}
	}

	private void enqueue(E x) {
		final Object[] items = this.items;
		items[putIndex] = x;
		if (++putIndex == items.length) {
			putIndex = 0;
		}
		count++;
		notEmpty.signal();
	}

	public E poll() {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			return (count == 0) ? null : dequeue();
		} finally {
			lock.unlock();
		}
	}

	public E poll(long timeout, TimeUnit unit) throws InterruptedException {
		long nanos = unit.toNanos(timeout);
		final ReentrantLock lock = this.lock;
		lock.lockInterruptibly();
		try {
			while (count == 0) {
				if (nanos <= 0) {
					return null;
				}
				nanos = notEmpty.awaitNanos(nanos);
			}
			return dequeue();
		} finally {
			lock.unlock();
		}
	}

	private E dequeue() {
		final Object[] items = this.items;
		@SuppressWarnings("unchecked")
		E x = (E) items[takeIndex];
		items[takeIndex] = null;
		if (++takeIndex == items.length) {
			takeIndex = 0;
		}
		count--;
		notFull.signal();
		return x;
	}
}
