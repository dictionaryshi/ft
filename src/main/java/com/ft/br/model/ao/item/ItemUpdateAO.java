package com.ft.br.model.ao.item;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 * ItemUpdateAO
 *
 * @author shichunyang
 */
@ApiModel("修改订单项")
@Getter
@Setter
@ToString
public class ItemUpdateAO {

    @NotNull(message = "订单项id不为null")
    @ApiModelProperty(value = "订单项id", required = true, example = "10")
    private Integer itemId;

    @NotNull(message = "商品id不为NULL")
    @ApiModelProperty(value = "商品id", required = true, example = "10")
    private Integer goodsId;

    @NotNull(message = "商品数量不为NULL")
    @DecimalMin(value = "0", inclusive = false, message = "商品数量必须大于0")
    @ApiModelProperty(value = "商品数量", required = true, example = "10")
    private Integer goodsNumber;
}
