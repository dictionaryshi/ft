package com.ft.br.constant;

import com.ft.util.JsonUtil;
import com.google.common.collect.ImmutableMap;

import java.util.stream.Stream;

/**
 * StockLogTypeDetailEnum
 *
 * @author shichunyang
 */
public enum StockLogTypeDetailEnum {
    /**
     * 出入库详细状态
     */
    IN_PERSON(10, "人工入库"),
    IN_ORDER(11, "订单入库"),
    OUT_PERSON(20, "人工出库"),
    OUT_ORDER(21, "订单出库");

    private Integer typeDetail;
    private String message;

    StockLogTypeDetailEnum(Integer typeDetail, String message) {
        this.typeDetail = typeDetail;
        this.message = message;
    }

    public Integer getTypeDetail() {
        return typeDetail;
    }

    public String getMessage() {
        return message;
    }

    public static StockLogTypeDetailEnum getByTypeDetail(int typeDetail) {
        return Stream.of(StockLogTypeDetailEnum.values())
                .filter(stockLogTypeDetailEnum -> stockLogTypeDetailEnum.typeDetail.equals(typeDetail))
                .findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return JsonUtil.object2Json(ImmutableMap.of(this.typeDetail, this.message));
    }
}
