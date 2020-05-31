package com.ft.br.constant;

import com.ft.util.JsonUtil;
import com.google.common.collect.ImmutableMap;

import java.util.stream.Stream;

/**
 * 订单状态
 *
 * @author shichunyang
 */
public enum OrderStatusEnum {
    /**
     * 订单状态
     */
    WAIT_TO_CONFIRMED(0, "待确认"),
    HAS_BEEN_CONFIRMED(1, "已确认"),
    SUCCESS(2, "成功"),
    FAIL(3, "失败");

    private Integer status;
    private String message;

    OrderStatusEnum(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public static OrderStatusEnum getByStatus(int status) {
        return Stream.of(OrderStatusEnum.values())
                .filter(orderStatusEnum -> orderStatusEnum.status.equals(status)).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return JsonUtil.object2Json(ImmutableMap.of(this.status, this.message));
    }
}
