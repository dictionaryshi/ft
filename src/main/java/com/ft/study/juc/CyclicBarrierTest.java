package com.ft.study.juc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 控制多个线程互相等待
 *
 * @author shichunyang
 */
public class CyclicBarrierTest {
	public static void main(String[] args) throws Exception {
		int totalThread = 10;
		CyclicBarrier cyclicBarrier = new CyclicBarrier(totalThread);
		ExecutorService executorService = ThreadPoolUtil.getThreadPool();
		for (int i = 0; i < totalThread; i++) {
			executorService.execute(() -> {
				System.out.println("before, thread==>" + Thread.currentThread().getName());
				try {
					cyclicBarrier.await();
				} catch (InterruptedException | BrokenBarrierException e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(5_000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("after, thread==>" + Thread.currentThread().getName());
			});
		}
		executorService.shutdown();
		while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
			System.out.println("线程池中仍然有线程执行");
		}
	}
}
