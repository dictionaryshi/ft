package com.ft.br.model.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * CategoryGetAO
 *
 * @author shichunyang
 */
@ApiModel("根据主键查询分类信息")
@Getter
@Setter
@ToString
public class CategoryGetAO {

	@ApiModelProperty(value = "分类主键", required = true)
	private Integer id;
}
