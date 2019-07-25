package com.ft.br.model.ao.item;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 * ItemAO
 *
 * @author shichunyang
 */
@ApiModel("添加订单项信息")
@Getter
@Setter
@ToString
public class ItemAO {

	@NotNull(message = "订单id不为NULL")
	@ApiModelProperty(value = "订单id", required = true, example = "10")
	private Long orderId;

	@NotNull(message = "商品id不为NULL")
	@ApiModelProperty(value = "商品id", required = true, example = "10")
	private Integer goodsId;

	@NotNull(message = "商品数量不为NULL")
	@DecimalMin(value = "0", inclusive = false, message = "商品数量必须大于0")
	@ApiModelProperty(value = "商品数量", required = true, example = "10")
	private Integer goodsNumber;
}
