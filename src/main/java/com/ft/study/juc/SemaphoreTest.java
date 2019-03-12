package com.ft.study.juc;

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
		ExecutorService executorService = ThreadPoolUtil.getThreadPool();
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
		executorService.shutdown();
		while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
			System.out.println("线程池中仍然有线程执行");
		}
	}
}
