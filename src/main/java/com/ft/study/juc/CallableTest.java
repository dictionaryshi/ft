package com.ft.study.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * CallableTest
 *
 * @author shichunyang
 */
@Slf4j
public class CallableTest {
	public static void main(String[] args) throws Exception {
		ExecutorService executorService = ThreadPoolUtil.getThreadPool();
		Future<Integer> future = executorService.submit(() -> {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return 100;
		});
		// 有闭锁的效果
		Integer result = future.get();
		log.info("result==>{}", result);
		executorService.shutdown();
	}
}
