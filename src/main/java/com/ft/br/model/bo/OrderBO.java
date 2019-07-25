package com.ft.br.model.bo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 订单信息
 *
 * @author shichunyang
 */
@ApiModel(value = "订单信息")
@Setter
@Getter
@ToString
public class OrderBO {

	private Long id;

	private String operatorName;

	private Integer status;

	private String statusCH;

	private String username;

	private String phone;

	private String address;

	private Integer totalAmount;

	private String remark;

	private String confirmTimeCH;

	private String finalOperateTimeCH;

	private String createdAtCH;
}
