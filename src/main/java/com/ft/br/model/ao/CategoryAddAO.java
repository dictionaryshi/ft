package com.ft.br.model.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * CategoryAddAO
 *
 * @author shichunyang
 */
@ApiModel("添加分类信息")
@Getter
@Setter
@ToString
public class CategoryAddAO {

	@NotBlank(message = "分类名称不能为空")
	@ApiModelProperty(value = "分类名称", required = true)
	private String name;
}
