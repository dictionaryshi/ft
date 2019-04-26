package com.ft.br.study.juc;

import com.ft.util.thread.ThreadPoolUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁应用
 *
 * @author shichunyang
 */
public class ReentrantReadWriteLockTest {
	private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

	public void write() {
		ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
		try {
			writeLock.lock();
			System.out.println(Thread.currentThread().getName() + ", 写开始");
			TimeUnit.SECONDS.sleep(1);
			System.out.println(Thread.currentThread().getName() + ", 写完成");
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			writeLock.unlock();
		}
	}

	public void read() {
		ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
		try {
			readLock.lock();
			System.out.println(Thread.currentThread().getName() + ", 读开始");
			TimeUnit.SECONDS.sleep(3);
			System.out.println(Thread.currentThread().getName() + ", 读完成");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			readLock.unlock();
		}
	}

	public static void main(String[] args) throws Exception {
		String poolName = "读写锁线程池";
		ExecutorService threadPool = ThreadPoolUtil.getThreadPool(poolName, 10, 20, 300, TimeUnit.SECONDS, 50, null);
		ReentrantReadWriteLockTest reentrantReadWriteLockTest = new ReentrantReadWriteLockTest();
		int target = 2;

		for (int i = 0; i < target; i++) {
			threadPool.submit(reentrantReadWriteLockTest::write);
			threadPool.submit(reentrantReadWriteLockTest::write);
			threadPool.submit(reentrantReadWriteLockTest::read);
			threadPool.submit(reentrantReadWriteLockTest::read);
		}

		ThreadPoolUtil.shutdown(threadPool, poolName);
	}
}
