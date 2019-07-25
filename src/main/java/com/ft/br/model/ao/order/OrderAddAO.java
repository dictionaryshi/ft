package com.ft.br.model.ao.order;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * OrderAddAO
 *
 * @author shichunyang
 */
@ApiModel("创建订单")
@Getter
@Setter
@ToString
public class OrderAddAO {

	private String username;

	private String phone;

	private String address;

	private Integer totalAmount;

	private String remark;

	private Integer operator;
}
