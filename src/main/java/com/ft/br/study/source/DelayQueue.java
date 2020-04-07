package com.ft.br.study.source;

import java.util.PriorityQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

@SuppressWarnings("unchecked")
public class DelayQueue<E extends Delayed> {

	private final transient ReentrantLock lock = new ReentrantLock();
	private final java.util.PriorityQueue<E> q = new PriorityQueue<E>();
	private Thread leader = null;
	private final Condition available = lock.newCondition();

	public boolean offer(E e) {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			q.offer(e);
			if (q.peek() == e) {
				leader = null;
				available.signal();
			}
			return true;
		} finally {
			lock.unlock();
		}
	}

	public E poll() {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			E first = q.peek();
			if (first == null || first.getDelay(NANOSECONDS) > 0) {
				return null;
			} else {
				return q.poll();
			}
		} finally {
			lock.unlock();
		}
	}

	public E take() throws InterruptedException {
		final ReentrantLock lock = this.lock;
		lock.lockInterruptibly();
		try {
			for (; ; ) {
				E first = q.peek();
				if (first == null) {
					available.await();
				} else {
					long delay = first.getDelay(NANOSECONDS);
					if (delay <= 0) {
						return q.poll();
					}
					first = null;
					if (leader != null) {
						available.await();
					} else {
						Thread thisThread = Thread.currentThread();
						leader = thisThread;
						try {
							available.awaitNanos(delay);
						} finally {
							if (leader == thisThread) {
								leader = null;
							}
						}
					}
				}
			}
		} finally {
			if (leader == null && q.peek() != null) {
				available.signal();
			}
			lock.unlock();
		}
	}

	public E peek() {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			return q.peek();
		} finally {
			lock.unlock();
		}
	}

	public int size() {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			return q.size();
		} finally {
			lock.unlock();
		}
	}

	private E peekExpired() {
		E first = q.peek();
		return (first == null || first.getDelay(NANOSECONDS) > 0) ?
				null : first;
	}
}
