package com.ft.br.service.hystrix;

import com.ft.web.cloud.hystrix.TtlHystrixConcurrencyStrategy;
import com.ft.web.constant.HystrixConstant;
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
		super(HystrixConstant.getSetter(
				"OrderGroup",
				"OrderPool",
				10,
				5,
				20,
				16,
				30,
				6000
		));
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
		HystrixPlugins.getInstance().registerConcurrencyStrategy(new TtlHystrixConcurrencyStrategy());

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
