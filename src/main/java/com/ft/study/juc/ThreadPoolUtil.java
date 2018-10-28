package com.ft.study.juc;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * ThreadPoolUtil
 *
 * @author shichunyang
 */
public class ThreadPoolUtil {

	/**
	 * 线程池
	 */
	private volatile static ExecutorService threadPool = null;

	/**
	 * 调度线程池
	 */
	private volatile static ScheduledExecutorService scheduledPool = null;

	private ThreadPoolUtil() {
	}

	/**
	 * 得到线程池
	 *
	 * @return 线程池对象
	 */
	public static ExecutorService getThreadPool() {
		if (threadPool == null) {
			synchronized (ThreadPoolUtil.class) {
				if (threadPool == null) {
					ThreadFactory threadFactory = new ThreadFactoryBuilder()
							.setNameFormat("ft-pool-%d").build();

					threadPool = new ThreadPoolExecutor(
							20,
							100,
							300,
							TimeUnit.SECONDS,
							new LinkedBlockingQueue<>(1024),
							threadFactory,
							new ThreadPoolExecutor.AbortPolicy()
					);
				}
			}
		}
		return threadPool;
	}

	/**
	 * 获取调度线程池
	 *
	 * @return 调度线程池
	 */
	public static ScheduledExecutorService getScheduledPool() {
		if (scheduledPool == null) {
			synchronized (ThreadPoolUtil.class) {
				if (scheduledPool == null) {
					ThreadFactory threadFactory = new ThreadFactoryBuilder()
							.setNameFormat("ft-scheduledPool-%d").build();
					scheduledPool = new ScheduledThreadPoolExecutor(20,
							threadFactory,
							new ThreadPoolExecutor.AbortPolicy());
				}
			}
		}
		return scheduledPool;
	}

	public static void main(String[] args) {
		ScheduledExecutorService scheduledExecutorService = getScheduledPool();
		scheduledExecutorService.scheduleWithFixedDelay(() -> {
			System.out.println(Math.random());
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Math.random());
		}, 5, 5, TimeUnit.SECONDS);
	}
}
