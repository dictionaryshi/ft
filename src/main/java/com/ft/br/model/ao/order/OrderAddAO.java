package com.ft.br.model.ao.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

	@NotBlank(message = "客户姓名不为空")
	@ApiModelProperty(value = "客户姓名", required = true)
	private String username;

	@NotBlank(message = "客户手机号不为空")
	@Size(min = 11, max = 11, message = "手机号必须为11位")
	@ApiModelProperty(value = "客户手机号", required = true)
	private String phone;

	@NotBlank(message = "送货地址不为空")
	@ApiModelProperty(value = "送货地址", required = true)
	private String address;

	@NotNull(message = "订单总金额不为NULL")
	@DecimalMin(value = "0", message = "订单金额最小为0")
	@ApiModelProperty(value = "订单总金额(元)", required = true, example = "25000")
	private Integer totalAmount;

	@ApiModelProperty(value = "订单备注")
	private String remark;

	private Integer operator;
}
