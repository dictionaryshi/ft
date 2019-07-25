package com.ft.br.model.bo;

import com.ft.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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

	@ApiModelProperty(value = "订单号", required = true, example = "2237896")
	private Long id;

	@ApiModelProperty(value = "操作人", required = true)
	private String operatorName;

	@ApiModelProperty(value = "订单状态(0:待确认, 1:已确认, 2:成功, 3:失败)", required = true, example = "1")
	private Integer status;

	@ApiModelProperty(value = "订单状态", required = true)
	private String statusCH;

	@ApiModelProperty(value = "客户姓名", required = true)
	private String username;

	@ApiModelProperty(value = "客户手机号", required = true)
	private String phone;

	@ApiModelProperty(value = "送货地址", required = true)
	private String address;

	@ApiModelProperty(value = "订单总金额", required = true, example = "38000")
	private Integer totalAmount;

	@ApiModelProperty(value = "订单备注")
	private String remark;

	@ApiModelProperty(value = "订单确认时间", example = DateUtil.TIMESTAMP_DEFAULT_TIME)
	private String confirmTimeCH;

	@ApiModelProperty(value = "最终操作时间", example = DateUtil.TIMESTAMP_DEFAULT_TIME)
	private String finalOperateTimeCH;

	@ApiModelProperty(value = "订单创建时间", example = DateUtil.TIMESTAMP_DEFAULT_TIME)
	private String createdAtCH;
}
