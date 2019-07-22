package com.ft.br.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * GoodsBO
 *
 * @author shichunyang
 */
@ApiModel(value = "商品信息")
@Setter
@Getter
@ToString
public class GoodsBO {

	@ApiModelProperty(value = "商品id", required = true, example = "10")
	private Integer id;

	@ApiModelProperty(value = "商品名称", required = true)
	private String goodsName;

	@ApiModelProperty(value = "库存数量", required = true, example = "10")
	private Integer stockNumber;

	@ApiModelProperty(value = "分类id", required = true, example = "10")
	private Integer categoryId;

	@ApiModelProperty(value = "分类名称", required = true)
	private String categoryName;
}
