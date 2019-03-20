package com.ft.study.juc;

import com.ft.util.thread.ThreadPoolUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 控制对互斥资源的访问线程数
 *
 * @author shichunyang
 */
public class SemaphoreTest {
	public static void main(String[] args) throws Exception {
		int clientCount = 3;
		int totalRequestCount = 10;
		Semaphore semaphore = new Semaphore(clientCount);
		String poolName = "semaphore";
		ExecutorService executorService = ThreadPoolUtil.getThreadPool(poolName, 10, 20, 300, TimeUnit.SECONDS, 50, null);
		for (int i = 0; i < totalRequestCount; i++) {
			executorService.execute(() -> {
				try {
					semaphore.acquire();
					System.out.println("剩余许可数==>" + semaphore.availablePermits());
					Thread.sleep(3_000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					semaphore.release();
				}
			});
		}
		ThreadPoolUtil.shutdown(executorService, poolName);
	}
}
