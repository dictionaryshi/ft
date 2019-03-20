package com.ft.study.juc;

import com.ft.util.thread.ThreadPoolUtil;

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
		String poolName = "cyclicBarrier";
		ExecutorService executorService = ThreadPoolUtil.getThreadPool(poolName, 10, 20, 300, TimeUnit.SECONDS, 50, null);
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
		ThreadPoolUtil.shutdown(executorService, poolName);
	}
}
