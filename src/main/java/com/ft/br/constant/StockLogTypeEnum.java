package com.ft.br.constant;

import com.ft.util.JsonUtil;
import com.google.common.collect.ImmutableMap;

import java.util.stream.Stream;

/**
 * StockLogTypeEnum
 *
 * @author shichunyang
 */
public enum StockLogTypeEnum {
    /**
     * 出入库状态
     */
    IN(1, "入库"),
    OUT(2, "出库");

    private Integer type;
    private String message;

    StockLogTypeEnum(Integer type, String message) {
        this.type = type;
        this.message = message;
    }

    public Integer getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public static StockLogTypeEnum getByType(int type) {
        return Stream.of(StockLogTypeEnum.values())
                .filter(stockLogTypeEnum -> stockLogTypeEnum.type.equals(type))
                .findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return JsonUtil.object2Json(ImmutableMap.of(this.type, this.message));
    }
}
