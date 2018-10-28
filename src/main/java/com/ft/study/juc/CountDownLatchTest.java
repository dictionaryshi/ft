package com.ft.study.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * CountDownLatchTest
 *
 * @author shichunyang
 */
@Slf4j
public class CountDownLatchTest {

	public static void main(String[] args) {

		int threadSize = 5;

		CountDownLatch countDownLatch = new CountDownLatch(threadSize);

		ExecutorService executorService = ThreadPoolUtil.getThreadPool();
		long start = System.currentTimeMillis();

		for (int i = 0; i < threadSize; i++) {
			executorService.submit(() -> {
				try {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					log.info(Thread.currentThread().getName());
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		executorService.shutdown();
	}
}
