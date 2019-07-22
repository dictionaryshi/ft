package com.ft.br.model.ao;

import com.ft.db.model.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

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

	@NotNull(message = "分类id不为null")
	@ApiModelProperty(value = "分类id", required = true, example = "10")
	private Integer categoryId;
}
