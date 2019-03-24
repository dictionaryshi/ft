package com.ft.br.constant;

import com.google.common.collect.ImmutableMap;

/**
 * 订单常量类
 *
 * @author shichunyang
 */
public class OrderConstant {

	/**
	 * 订单准备中
	 */
	public static final short STATUS_READY = 0;
	/**
	 * 确认订单
	 */
	public static final short STATUS_CONFIRM = 1;
	/**
	 * 订单成功
	 */
	public static final short STATUS_SUCCESS = 2;
	/**
	 * 订单失败
	 */
	public static final short STATUS_FAIL = 3;
	/**
	 * 订单状态中文map
	 */
	public static final ImmutableMap<Short, String> STATUS_MAP = ImmutableMap.<Short, String>builder()
			.put(STATUS_READY, "待确认")
			.put(STATUS_CONFIRM, "已确认")
			.put(STATUS_SUCCESS, "成功")
			.put(STATUS_FAIL, "失败")
			.build();
}
