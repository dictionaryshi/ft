package com.ft.br.study.source;

import com.ft.br.study.juc.CasTest;
import sun.misc.Unsafe;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@SuppressWarnings("unchecked")
public class ReentrantLock {
	private final Sync sync;

	public ReentrantLock() {
		sync = new NonfairSync();
	}

	public ReentrantLock(boolean fair) {
		sync = fair ? new FairSync() : new NonfairSync();
	}

	public void lock() {
		sync.lock();
	}

	public void lockInterruptibly() throws InterruptedException {
		sync.acquireInterruptibly(1);
	}

	public boolean tryLock() {
		return sync.nonfairTryAcquire(1);
	}

	public boolean tryLock(long timeout, TimeUnit unit)
			throws InterruptedException {
		return sync.tryAcquireNanos(1, unit.toNanos(timeout));
	}

	public void unlock() {
		sync.release(1);
	}

	static final class NonfairSync extends Sync {
		private static final long serialVersionUID = 7316153563782823691L;

		@Override
		final void lock() {
			if (compareAndSetState(0, 1)) {
				setExclusiveOwnerThread(Thread.currentThread());
			} else {
				acquire(1);
			}
		}

		/**
		 * true:获取锁成功
		 */
		@Override
		protected final boolean tryAcquire(int acquires) {
			return nonfairTryAcquire(acquires);
		}
	}

	static final class FairSync extends Sync {
		private static final long serialVersionUID = -3000897897090466540L;

		@Override
		final void lock() {
			acquire(1);
		}

		@Override
		protected final boolean tryAcquire(int acquires) {
			final Thread current = Thread.currentThread();
			int c = getState();
			if (c == 0) {
				if (!hasQueuedPredecessors() &&
						compareAndSetState(0, acquires)) {
					setExclusiveOwnerThread(current);
					return true;
				}
			} else if (current == getExclusiveOwnerThread()) {
				int nextc = c + acquires;
				setState(nextc);
				return true;
			}
			return false;
		}
	}

	abstract static class Sync {
		private static final long serialVersionUID = -5179523762034025860L;

		private static final Unsafe unsafe = CasTest.UNSAFE;

		private static final long stateOffset;
		private static final long headOffset;
		private static final long tailOffset;
		private static final long waitStatusOffset;
		private static final long nextOffset;

		static {
			try {
				stateOffset = unsafe.objectFieldOffset
						(Sync.class.getDeclaredField("state"));
				tailOffset = unsafe.objectFieldOffset
						(Sync.class.getDeclaredField("tail"));
				headOffset = unsafe.objectFieldOffset
						(Sync.class.getDeclaredField("head"));
				waitStatusOffset = unsafe.objectFieldOffset
						(Node.class.getDeclaredField("waitStatus"));
				nextOffset = unsafe.objectFieldOffset
						(Node.class.getDeclaredField("next"));
			} catch (Exception e) {
				throw new java.lang.Error(e);
			}
		}

		final boolean compareAndSetState(int expect, int update) {
			return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
		}

		private boolean compareAndSetTail(Node expect, Node update) {
			return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
		}

		private boolean compareAndSetHead(Node update) {
			return unsafe.compareAndSwapObject(this, headOffset, null, update);
		}

		private static boolean compareAndSetWaitStatus(Node node,
													   int expect,
													   int update) {
			return unsafe.compareAndSwapInt(node, waitStatusOffset,
					expect, update);
		}

		private static boolean compareAndSetNext(Node node,
												 Node expect,
												 Node update) {
			return unsafe.compareAndSwapObject(node, nextOffset, expect, update);
		}

		private volatile int state;

		private transient volatile Node head;
		private transient volatile Node tail;

		private transient Thread exclusiveOwnerThread;

		static final long spinForTimeoutThreshold = 1000L;

		static final class Node {
			static final Node EXCLUSIVE = null;
			static final int CANCELLED = 1;
			static final int SIGNAL = -1;

			volatile int waitStatus;

			volatile Node prev;

			volatile Node next;

			volatile Thread thread;

			Node nextWaiter;

			final Node predecessor() throws NullPointerException {
				Node p = prev;
				if (p == null) {
					throw new NullPointerException();
				} else {
					return p;
				}
			}

			Node() {
			}

			Node(Thread thread, Node mode) {
				this.nextWaiter = mode;
				this.thread = thread;
			}
		}

		final int getState() {
			return state;
		}

		final void setState(int newState) {
			state = newState;
		}

		final Thread getExclusiveOwnerThread() {
			return exclusiveOwnerThread;
		}

		/**
		 * 设置当前持有锁的线程
		 */
		final void setExclusiveOwnerThread(Thread thread) {
			exclusiveOwnerThread = thread;
		}

		abstract void lock();

		final void acquire(int arg) {
			if (!tryAcquire(arg) &&
					acquireQueued(addWaiter(Node.EXCLUSIVE), arg)) {
				selfInterrupt();
			}
		}

		protected boolean tryAcquire(int arg) {
			throw new UnsupportedOperationException();
		}

		/**
		 * true:获取锁成功
		 */
		final boolean nonfairTryAcquire(int acquires) {
			final Thread current = Thread.currentThread();
			int c = getState();
			if (c == 0) {
				if (compareAndSetState(0, acquires)) {
					setExclusiveOwnerThread(current);
					return true;
				}
			} else if (current == getExclusiveOwnerThread()) {
				int nextc = c + acquires;
				setState(nextc);
				return true;
			}
			return false;
		}

		/**
		 * 将node添加到队尾
		 */
		private Node addWaiter(Node mode) {
			Node node = new Node(Thread.currentThread(), mode);
			Node pred = tail;
			if (pred != null) {
				node.prev = pred;
				if (compareAndSetTail(pred, node)) {
					pred.next = node;
					return node;
				}
			}
			enq(node);
			return node;
		}

		private Node enq(final Node node) {
			for (; ; ) {
				Node t = tail;
				if (t == null) {
					if (compareAndSetHead(new Node())) {
						tail = head;
					}
				} else {
					node.prev = t;
					if (compareAndSetTail(t, node)) {
						t.next = node;
						return t;
					}
				}
			}
		}

		final boolean acquireQueued(final Node node, int arg) {
			boolean failed = true;
			try {
				boolean interrupted = false;
				for (; ; ) {
					final Node p = node.predecessor();
					// 若当前任务的前一个节点是头节点, 则尝试获取一次锁
					if (p == head && tryAcquire(arg)) {
						setHead(node);
						p.next = null;
						failed = false;
						return interrupted;
					}
					if (shouldParkAfterFailedAcquire(p, node) &&
							parkAndCheckInterrupt()) {
						interrupted = true;
					}
				}
			} finally {
				if (failed) {
					cancelAcquire(node);
				}
			}
		}

		private void setHead(Node node) {
			head = node;
			node.thread = null;
			node.prev = null;
		}

		private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
			int ws = pred.waitStatus;
			if (ws == Node.SIGNAL) {
				return true;
			}
			if (ws > 0) {
				do {
					node.prev = pred = pred.prev;
				} while (pred.waitStatus > 0);
				pred.next = node;
			} else {
				compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
			}
			return false;
		}

		/**
		 * true:当前线程已中断, 并且已经清除中断标志。此时线程恢复运行
		 */
		private boolean parkAndCheckInterrupt() {
			// 阻塞当前线程
			LockSupport.park(this);
			// 清除当前线程的中断标志
			return Thread.interrupted();
		}

		private void cancelAcquire(Node node) {
			if (node == null) {
				return;
			}

			node.thread = null;

			Node pred = node.prev;
			while (pred.waitStatus > 0) {
				node.prev = pred = pred.prev;
			}

			Node predNext = pred.next;

			node.waitStatus = Node.CANCELLED;

			if (node == tail && compareAndSetTail(node, pred)) {
				compareAndSetNext(pred, predNext, null);
			} else {
				int ws;
				if (pred != head &&
						((ws = pred.waitStatus) == Node.SIGNAL ||
								(ws <= 0 && compareAndSetWaitStatus(pred, ws, Node.SIGNAL))) &&
						pred.thread != null) {
					Node next = node.next;
					if (next != null && next.waitStatus <= 0) {
						compareAndSetNext(pred, predNext, next);
					}
				} else {
					unparkSuccessor(node);
				}

				node.next = node;
			}
		}

		private void unparkSuccessor(Node node) {
			int ws = node.waitStatus;
			if (ws < 0) {
				compareAndSetWaitStatus(node, ws, 0);
			}

			Node s = node.next;
			if (s == null || s.waitStatus > 0) {
				s = null;
				for (Node t = tail; t != null && t != node; t = t.prev) {
					if (t.waitStatus <= 0) {
						s = t;
					}
				}
			}
			if (s != null) {
				LockSupport.unpark(s.thread);
			}
		}

		/**
		 * 给当前线程做一个中断标记
		 */
		static void selfInterrupt() {
			Thread.currentThread().interrupt();
		}

		final boolean hasQueuedPredecessors() {
			Node t = tail;
			Node h = head;
			Node s;
			return h != t &&
					((s = h.next) == null || s.thread != Thread.currentThread());
		}

		final void acquireInterruptibly(int arg)
				throws InterruptedException {
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			if (!tryAcquire(arg)) {
				doAcquireInterruptibly(arg);
			}
		}

		private void doAcquireInterruptibly(int arg)
				throws InterruptedException {
			final Node node = addWaiter(Node.EXCLUSIVE);
			boolean failed = true;
			try {
				for (; ; ) {
					final Node p = node.predecessor();
					if (p == head && tryAcquire(arg)) {
						setHead(node);
						p.next = null;
						failed = false;
						return;
					}
					if (shouldParkAfterFailedAcquire(p, node) &&
							parkAndCheckInterrupt()) {
						throw new InterruptedException();
					}
				}
			} finally {
				if (failed) {
					cancelAcquire(node);
				}
			}
		}

		final boolean tryAcquireNanos(int arg, long nanosTimeout)
				throws InterruptedException {
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			return tryAcquire(arg) ||
					doAcquireNanos(arg, nanosTimeout);
		}

		private boolean doAcquireNanos(int arg, long nanosTimeout)
				throws InterruptedException {
			if (nanosTimeout <= 0L) {
				return false;
			}
			final long deadline = System.nanoTime() + nanosTimeout;
			final Node node = addWaiter(Node.EXCLUSIVE);
			boolean failed = true;
			try {
				for (; ; ) {
					final Node p = node.predecessor();
					if (p == head && tryAcquire(arg)) {
						setHead(node);
						p.next = null;
						failed = false;
						return true;
					}
					nanosTimeout = deadline - System.nanoTime();
					if (nanosTimeout <= 0L) {
						return false;
					}
					if (shouldParkAfterFailedAcquire(p, node) &&
							nanosTimeout > spinForTimeoutThreshold) {
						LockSupport.parkNanos(this, nanosTimeout);
					}
					if (Thread.interrupted()) {
						throw new InterruptedException();
					}
				}
			} finally {
				if (failed) {
					cancelAcquire(node);
				}
			}
		}

		final boolean release(int arg) {
			if (tryRelease(arg)) {
				Node h = head;
				if (h != null && h.waitStatus != 0) {
					unparkSuccessor(h);
				}
				return true;
			}
			return false;
		}

		final boolean tryRelease(int releases) {
			int c = getState() - releases;
			if (Thread.currentThread() != getExclusiveOwnerThread()) {
				throw new IllegalMonitorStateException();
			}
			boolean free = false;
			if (c == 0) {
				free = true;
				setExclusiveOwnerThread(null);
			}
			setState(c);
			return free;
		}
	}
}
