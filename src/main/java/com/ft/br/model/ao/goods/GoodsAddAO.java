package com.ft.br.model.ao.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * GoodsAddAO
 *
 * @author shichunyang
 */
@ApiModel("添加商品信息")
@Getter
@Setter
@ToString
public class GoodsAddAO {

	@NotBlank(message = "商品名称不为空")
	@ApiModelProperty(value = "商品名称", required = true)
	private String name;

	@NotNull(message = "分类id不为null")
	@ApiModelProperty(value = "分类id", required = true, example = "10")
	private Integer categoryId;
}
