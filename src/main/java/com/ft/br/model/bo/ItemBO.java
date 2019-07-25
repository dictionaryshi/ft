package com.ft.br.model.bo;

import com.ft.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * ItemBO
 *
 * @author shichunyang
 */
@ApiModel(value = "订单项信息")
@Setter
@Getter
@ToString
public class ItemBO {

	@ApiModelProperty(value = "订单项id", required = true, example = "10")
	private Integer id;

	@ApiModelProperty(value = "订单id", required = true, example = "10")
	private Long orderId;

	@ApiModelProperty(value = "商品id", required = true, example = "10")
	private Integer goodsId;

	@ApiModelProperty(value = "商品数量", required = true, example = "10")
	private Integer goodsNumber;

	@ApiModelProperty(value = "订单项创建时间", required = true, example = DateUtil.TIMESTAMP_DEFAULT_TIME)
	private Date createdAt;

	@ApiModelProperty(value = "商品名称", required = true)
	private String goodsName;
}
