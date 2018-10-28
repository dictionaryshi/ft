package com.ft.constant;

import com.google.common.collect.ImmutableMap;

/**
 * 仓库常量类
 *
 * @author shichunyang
 */
public class StockConstant {
	/**
	 * 入库
	 */
	public static final short TYPE_IN = 1;
	/**
	 * 人工入库
	 */
	public static final short TYPE_DETAIL_IN_PERSON = 10;
	/**
	 * 订单入库
	 */
	public static final short TYPE_DETAIL_IN_ORDER = 11;

	/**
	 * 出库
	 */
	public static final short TYPE_OUT = 2;
	/**
	 * 人工出库
	 */
	public static final short TYPE_DETAIL_OUT_PERSON = 20;
	/**
	 * 订单出库
	 */
	public static final short TYPE_DETAIL_OUT_ORDER = 21;

	/**
	 * 出入库中文对照
	 */
	public static final ImmutableMap<Short, String> TYPE_MAP = ImmutableMap.<Short, String>builder()
			.put(TYPE_IN, "入库")
			.put(TYPE_OUT, "出库")
			.build();

	/**
	 * 出入库详细类型中文对照
	 */
	public static final ImmutableMap<Short, String> TYPE_DETAIL_MAP = ImmutableMap.<Short, String>builder()
			.put(TYPE_DETAIL_IN_PERSON, "人工入库")
			.put(TYPE_DETAIL_OUT_PERSON, "人工出库")
			.put(TYPE_DETAIL_OUT_ORDER, "订单出库")
			.put(TYPE_DETAIL_IN_ORDER, "订单入库")
			.build();
}
