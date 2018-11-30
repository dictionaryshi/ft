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
	 * (这里的volatile关键字主要是为了防止指令重排, new ThreadPoolExecutor() 分为三步, 1:分配内存空间; 2:初始化对象; 3:将threadPool对象指向分配的内存地址。
	 * 可能第三步在第二步之前被执行就有可能导致某个线程拿到的单例对象还没有初始化。)
	 */
	private static volatile ExecutorService threadPool = null;

	/**
	 * 调度线程池
	 */
	private static volatile ScheduledExecutorService scheduledPool = null;

	private ThreadPoolUtil() {
	}

	/**
	 * 得到线程池
	 * corePoolSize:线程池的基本大小
	 * maximumPoolSize:线程池最大线程大小
	 * keepAliveTime:线程空闲后的存活时间
	 * workQueue:存放任务的阻塞队列
	 * handler:当队列和最大线程池都满了之后的饱和策略
	 *
	 * @return 线程池对象
	 */
	public static ExecutorService getThreadPool() {
		if (threadPool == null) {
			synchronized (ThreadPoolUtil.class) {
				if (threadPool == null) {
					ThreadFactory threadFactory = new ThreadFactoryBuilder()
							.setNameFormat("ft-pool-%d").build();

					int cpuNumber = getCpuNumber();

					threadPool = new ThreadPoolExecutor(
							cpuNumber * 3,
							cpuNumber * 5,
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
					int cpuNumber = getCpuNumber();

					ThreadFactory threadFactory = new ThreadFactoryBuilder()
							.setNameFormat("ft-scheduledPool-%d").build();
					scheduledPool = new ScheduledThreadPoolExecutor(cpuNumber * 5,
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

	/**
	 * 获取CPU个数
	 *
	 * @return CPU个数
	 */
	public static int getCpuNumber() {
		Runtime runtime = Runtime.getRuntime();
		return runtime.availableProcessors();
	}
}
