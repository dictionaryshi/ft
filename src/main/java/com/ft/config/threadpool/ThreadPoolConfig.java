package com.ft.config.threadpool;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池配置
 *
 * @author shichunyang
 */
@Configuration
public class ThreadPoolConfig {

	/**
	 * 初始化线程池
	 *
	 * @return 线程池
	 */
	//@Bean
	public ThreadPoolTaskExecutor threadPool() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(20);
		threadPoolTaskExecutor.setMaxPoolSize(80);
		threadPoolTaskExecutor.setQueueCapacity(1024);
		threadPoolTaskExecutor.setKeepAliveSeconds(300);
		threadPoolTaskExecutor.initialize();
		return threadPoolTaskExecutor;
	}
}
