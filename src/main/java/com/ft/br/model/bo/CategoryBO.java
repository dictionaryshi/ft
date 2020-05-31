package com.ft.br.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * CategoryBO
 *
 * @author shichunyang
 */
@ApiModel("分类信息")
@Getter
@Setter
@ToString
public class CategoryBO {

    @ApiModelProperty(value = "分类id", required = true)
    private Integer id;

    @ApiModelProperty(value = "分类名称", required = true)
    private String name;
}
