package com.ft.br.model.ao.stock;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * StockLogStorageAO
 *
 * @author shichunyang
 */
@ApiModel("出入库操作")
@Getter
@Setter
@ToString
public class StockLogStorageAO {

	@NotNull(message = "仓库操作类型不为null")
	@ApiModelProperty(value = "仓库操作类型(1:入库, 2:出库)", required = true, example = "1")
	private Integer type;

	@NotNull(message = "商品id不为null")
	@ApiModelProperty(value = "商品id", required = true, example = "10")
	private Integer goodsId;

	@ApiModelProperty(value = "订单id", example = "10")
	private Long orderId;

	@NotNull(message = "出/入库数量不为null")
	@DecimalMin(value = "0", inclusive = false, message = "出/入库数量必须大于0")
	@ApiModelProperty(value = "出/入库数量", required = true, example = "10")
	private Integer storageNumber;

	@NotBlank(message = "操作备注不为空")
	@ApiModelProperty(value = "操作备注", required = true)
	private String remark;


	@ApiModelProperty(value = "操作人", hidden = true, example = "10")
	private Integer operator;
}
