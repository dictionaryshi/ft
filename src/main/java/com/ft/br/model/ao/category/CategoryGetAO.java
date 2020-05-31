package com.ft.br.model.ao.category;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

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

    @NotNull(message = "分类主键不为null")
    @ApiModelProperty(value = "分类主键", required = true, example = "10")
    private Integer id;
}
