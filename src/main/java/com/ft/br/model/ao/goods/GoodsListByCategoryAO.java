package com.ft.br.model.ao.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * GoodsListByCategoryAO
 *
 * @author shichunyang
 */
@ApiModel("根据分类查询所有商品")
@Getter
@Setter
@ToString
public class GoodsListByCategoryAO {

    @NotNull(message = "分类id不为null")
    @ApiModelProperty(value = "分类id", required = true, example = "10")
    private Integer categoryId;
}
