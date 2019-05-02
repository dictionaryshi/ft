package com.ft.br.study.source;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("unchecked")
public class LinkedBlockingQueue<E> {
	private final int capacity;

	private final AtomicInteger count = new AtomicInteger();

	transient Node<E> head;

	private transient Node<E> last;

	private final ReentrantLock takeLock = new ReentrantLock();
	private final Condition notEmpty = takeLock.newCondition();

	private final ReentrantLock putLock = new ReentrantLock();
	private final Condition notFull = putLock.newCondition();

	public LinkedBlockingQueue() {
		this(Integer.MAX_VALUE);
	}

	public LinkedBlockingQueue(int capacity) {
		if (capacity <= 0) {
			throw new IllegalArgumentException();
		}
		this.capacity = capacity;
		last = head = new Node<E>(null);
	}

	static class Node<E> {
		E item;

		Node<E> next;

		Node(E x) {
			item = x;
		}
	}

	public boolean offer(E e) {
		if (e == null) {
			throw new NullPointerException();
		}
		final AtomicInteger count = this.count;
		if (count.get() == capacity) {
			return false;
		}
		int c = -1;
		Node<E> node = new Node<E>(e);
		final ReentrantLock putLock = this.putLock;
		putLock.lock();
		try {
			if (count.get() < capacity) {
				enqueue(node);
				c = count.getAndIncrement();
				if (c + 1 < capacity) {
					notFull.signal();
				}
			}
		} finally {
			putLock.unlock();
		}
		if (c == 0) {
			signalNotEmpty();
		}
		return c >= 0;
	}

	public boolean offer(E e, long timeout, TimeUnit unit)
			throws InterruptedException {

		if (e == null) {
			throw new NullPointerException();
		}
		long nanos = unit.toNanos(timeout);
		int c = -1;
		final ReentrantLock putLock = this.putLock;
		final AtomicInteger count = this.count;
		putLock.lockInterruptibly();
		try {
			while (count.get() == capacity) {
				if (nanos <= 0) {
					return false;
				}
				nanos = notFull.awaitNanos(nanos);
			}
			enqueue(new Node<E>(e));
			c = count.getAndIncrement();
			if (c + 1 < capacity) {
				notFull.signal();
			}
		} finally {
			putLock.unlock();
		}
		if (c == 0) {
			signalNotEmpty();
		}
		return true;
	}

	private void enqueue(Node<E> node) {
		last = last.next = node;
	}

	private void signalNotEmpty() {
		final ReentrantLock takeLock = this.takeLock;
		takeLock.lock();
		try {
			notEmpty.signal();
		} finally {
			takeLock.unlock();
		}
	}

	public E poll() {
		final AtomicInteger count = this.count;
		if (count.get() == 0) {
			return null;
		}
		E x = null;
		int c = -1;
		final ReentrantLock takeLock = this.takeLock;
		takeLock.lock();
		try {
			if (count.get() > 0) {
				x = dequeue();
				c = count.getAndDecrement();
				if (c > 1) {
					notEmpty.signal();
				}
			}
		} finally {
			takeLock.unlock();
		}
		if (c == capacity) {
			signalNotFull();
		}
		return x;
	}

	public E poll(long timeout, TimeUnit unit) throws InterruptedException {
		E x = null;
		int c = -1;
		long nanos = unit.toNanos(timeout);
		final AtomicInteger count = this.count;
		final ReentrantLock takeLock = this.takeLock;
		takeLock.lockInterruptibly();
		try {
			while (count.get() == 0) {
				if (nanos <= 0) {
					return null;
				}
				nanos = notEmpty.awaitNanos(nanos);
			}
			x = dequeue();
			c = count.getAndDecrement();
			if (c > 1) {
				notEmpty.signal();
			}
		} finally {
			takeLock.unlock();
		}
		if (c == capacity) {
			signalNotFull();
		}
		return x;
	}

	private E dequeue() {
		Node<E> h = head;
		Node<E> first = h.next;
		h.next = h;
		head = first;
		E x = first.item;
		first.item = null;
		return x;
	}

	private void signalNotFull() {
		final ReentrantLock putLock = this.putLock;
		putLock.lock();
		try {
			notFull.signal();
		} finally {
			putLock.unlock();
		}
	}

	public void put(E e) throws InterruptedException {
		if (e == null) {
			throw new NullPointerException();
		}
		int c = -1;
		Node<E> node = new Node<E>(e);
		final ReentrantLock putLock = this.putLock;
		final AtomicInteger count = this.count;
		putLock.lockInterruptibly();
		try {
			while (count.get() == capacity) {
				notFull.await();
			}
			enqueue(node);
			c = count.getAndIncrement();
			if (c + 1 < capacity) {
				notFull.signal();
			}
		} finally {
			putLock.unlock();
		}
		if (c == 0) {
			signalNotEmpty();
		}
	}

	public E take() throws InterruptedException {
		E x;
		int c = -1;
		final AtomicInteger count = this.count;
		final ReentrantLock takeLock = this.takeLock;
		takeLock.lockInterruptibly();
		try {
			while (count.get() == 0) {
				notEmpty.await();
			}
			x = dequeue();
			c = count.getAndDecrement();
			if (c > 1) {
				notEmpty.signal();
			}
		} finally {
			takeLock.unlock();
		}
		if (c == capacity) {
			signalNotFull();
		}
		return x;
	}
}
