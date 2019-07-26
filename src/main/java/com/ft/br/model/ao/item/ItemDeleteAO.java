package com.ft.br.model.ao.item;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * ItemDeleteAO
 *
 * @author shichunyang
 */
@ApiModel("删除订单项")
@Getter
@Setter
@ToString
public class ItemDeleteAO {

	@NotNull(message = "订单项id不为null")
	@ApiModelProperty(value = "订单项id", required = true, example = "10")
	private Integer itemId;
}
