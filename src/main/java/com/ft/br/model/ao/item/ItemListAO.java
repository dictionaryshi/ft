package com.ft.br.model.ao.item;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * ItemListAO
 *
 * @author shichunyang
 */
@ApiModel("查询订单下的所有订单项")
@Getter
@Setter
@ToString
public class ItemListAO {

    @NotNull(message = "订单id不为NULL")
    @ApiModelProperty(value = "订单id", required = true, example = "10")
    private Long orderId;
}
