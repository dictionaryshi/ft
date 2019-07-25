package com.ft.br.model.bo;

import io.swagger.annotations.ApiModel;
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

	private Integer id;

	private Long orderId;

	private Integer goodsId;

	private Integer goodsNumber;

	private Date createdAt;

	private String goodsName;
}
