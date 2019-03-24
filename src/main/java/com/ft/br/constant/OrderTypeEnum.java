package com.ft.br.constant;

import com.ft.br.util.JsonUtil;
import com.ft.util.JsonUtil;
import com.google.common.collect.ImmutableMap;

import java.util.stream.Stream;

/**
 * 订单状态枚举
 *
 * @author shichunyang
 */
public enum OrderTypeEnum {

	/**
	 * 订单准备中
	 */
	READY(OrderConstant.STATUS_READY, OrderConstant.STATUS_MAP.get(OrderConstant.STATUS_READY)),
	CONFIRM(OrderConstant.STATUS_CONFIRM, OrderConstant.STATUS_MAP.get(OrderConstant.STATUS_CONFIRM)),
	SUCCESS(OrderConstant.STATUS_SUCCESS, OrderConstant.STATUS_MAP.get(OrderConstant.STATUS_SUCCESS)),
	FAIL(OrderConstant.STATUS_FAIL, OrderConstant.STATUS_MAP.get(OrderConstant.STATUS_FAIL)),
	UNKNOWN((short) -1, "未知订单状态");

	private Short status;
	private String message;

	OrderTypeEnum(short status, String message) {
		this.status = status;
		this.message = message;
	}

	public static OrderTypeEnum get(short status) {
		return Stream.of(OrderTypeEnum.values())
				.filter(orderType -> orderType.status.equals(status)).findFirst().orElse(UNKNOWN);
	}

	public Short getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return JsonUtil.object2Json(ImmutableMap.of(this.status, this.message));
	}
}
