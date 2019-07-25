package com.ft.br.model.ao.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * OrderGetAO
 *
 * @author shichunyang
 */
@ApiModel(value = "根据id查询订单信息")
@Setter
@Getter
@ToString
public class OrderGetAO {

	@NotNull(message = "订单id不为null")
	@ApiModelProperty(value = "订单id", required = true, example = "2237867")
	private Long id;
}
