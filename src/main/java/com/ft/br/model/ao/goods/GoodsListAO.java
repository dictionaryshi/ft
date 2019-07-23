package com.ft.br.model.ao.goods;

import com.ft.db.model.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * GoodsListAO
 *
 * @author shichunyang
 */
@ApiModel("分页查询商品信息")
@Getter
@Setter
@ToString
public class GoodsListAO extends PageParam {

	@ApiModelProperty(value = "分类id", example = "10")
	private Integer categoryId;
}