package com.ft.br.model.ao.category;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * CategoryUpdateAO
 *
 * @author shichunyang
 */
@ApiModel("修改分类信息")
@Getter
@Setter
@ToString
public class CategoryUpdateAO {

	@NotNull(message = "分类主键不为null")
	@ApiModelProperty(value = "分类主键", required = true, example = "10")
	private Integer id;

	@NotBlank(message = "分类名称不能为空")
	@ApiModelProperty(value = "分类名称", required = true)
	private String name;
}
