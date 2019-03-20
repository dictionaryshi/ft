package com.ft.study.juc;

import com.ft.util.thread.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * VolatileTest
 *
 * @author shichunyang
 */
@Slf4j
public class VolatileTest {

	private volatile static boolean flag = false;

	/**
	 * 1.要想并发程序正确地执行,必须要保证原子性、可见性以及有序性。只要有一个没有被保证,就有可能会导致程序运行不正确。
	 * 2.volatile修饰类的成员变量、类的静态成员变量。
	 * 3.内存可见性问题:多个线程操作共享数据时, 彼此不可见。
	 */
	public static void main(String[] args) throws Exception {
		String poolName = "volatile";
		ExecutorService threadPool = ThreadPoolUtil.getThreadPool(poolName, 10, 20, 300, TimeUnit.SECONDS, 50, null);

		for (int i = 0; i < 100; i++) {

			threadPool.submit(() -> {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				flag = true;
				log.info("thread==>{}, flag==>{}", Thread.currentThread().getName(), flag);
			});
		}
		while (true) {
			if (flag) {
				log.info("while break");
				break;
			}
		}

		ThreadPoolUtil.shutdown(threadPool, poolName);
	}
}
