package com.ft.br.model.ao.order;

import com.ft.db.model.PageParam;
import com.ft.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * OrderListAO
 *
 * @author shichunyang
 */
@ApiModel("分页查询订单信息")
@Getter
@Setter
@ToString
public class OrderListAO extends PageParam {

	@ApiModelProperty(value = "订单id", example = "1154268096047874000")
	private Long id;

	@ApiModelProperty(value = "订单状态(0:待确认, 1:已确认, 2:成功, 3:失败)", example = "1")
	private Integer status;

	@ApiModelProperty(value = "订单创建开始时间", example = DateUtil.TIMESTAMP_DEFAULT_TIME)
	private String startTime;

	@ApiModelProperty(value = "订单创建结束时间", example = DateUtil.TIMESTAMP_DEFAULT_TIME)
	private String endTime;
}
