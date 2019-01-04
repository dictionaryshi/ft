package com.ft.service.hystrix;

import com.ft.web.cloud.hystrix.ThreadLocalHystrixConcurrencyStrategy;
import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.HystrixPlugins;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Future;

/**
 * OrderCommand
 *
 * @author shichunyang
 */
@Slf4j
public class OrderCommand extends HystrixCommand<String> {
	private String orderParam;

	public OrderCommand(String orderParam) {
		super(Setter
				// 服务分组
				.withGroupKey(HystrixCommandGroupKey.Factory.asKey("OrderGroup"))
				// 线程分组
				.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("OrderPool"))

				// 线程池配置
				.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
						.withCoreSize(10)
						.withKeepAliveTimeMinutes(5)
						.withMaxQueueSize(20)
						.withQueueSizeRejectionThreshold(16)
				)

				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
						.withExecutionTimeoutInMilliseconds(3_000)
						.withExecutionIsolationThreadInterruptOnTimeout(Boolean.TRUE)
						.withExecutionIsolationThreadInterruptOnFutureCancel(Boolean.TRUE)
				)
		);
		this.orderParam = orderParam;
	}

	@Override
	public String run() {
		long start = System.currentTimeMillis();
		int target = 5000;
		for (int i = 0; i < target; i++) {
			for (int j = 0; j < target; j++) {
				System.out.print("");
			}
		}
		long end = System.currentTimeMillis();
		log.info("orderParam==>{}, cost==>{}", orderParam, end - start);
		return orderParam + ", success";
	}

	@Override
	public String getFallback() {
		return orderParam + ", fallback";
	}

	public static void main(String[] args) throws Exception {
		// 托管hystrix线程池
		HystrixPlugins.getInstance().registerConcurrencyStrategy(new ThreadLocalHystrixConcurrencyStrategy());

		OrderCommand mac = new OrderCommand("电脑");
		OrderCommand phone = new OrderCommand("手机");

		// 异步非阻塞
		Future<String> macResult = mac.queue();

		// 阻塞方式执行
		String phoneResult = phone.execute();

		System.out.println(phoneResult);
		System.out.println(macResult.get());
	}
}
