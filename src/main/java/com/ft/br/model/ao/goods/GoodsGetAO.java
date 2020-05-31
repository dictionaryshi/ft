package com.ft.br.model.ao.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * GoodsGetAO
 *
 * @author shichunyang
 */
@ApiModel("根据id查询商品信息")
@Getter
@Setter
@ToString
public class GoodsGetAO {

    @NotNull(message = "商品id不为null")
    @ApiModelProperty(value = "商品id", required = true, example = "10")
    private Integer id;
}
