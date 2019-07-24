package com.ft.br.model.ao.stock;

import com.ft.db.model.PageParam;
import com.ft.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * StockLogListAO
 *
 * @author shichunyang
 */
@ApiModel("分页查询仓库操作信息")
@Getter
@Setter
@ToString
public class StockLogListAO extends PageParam {

	@ApiModelProperty(value = "仓库操作类型(1:入库, 2:出库)", example = "1")
	private Integer type;

	@ApiModelProperty(value = "商品id", example = "10")
	private Integer goodsId;

	@ApiModelProperty(value = "订单id", example = "10")
	private Long orderId;

	@ApiModelProperty(value = "操作开始日期", example = DateUtil.TIMESTAMP_DEFAULT_TIME)
	private String startTime;

	@ApiModelProperty(value = "操作结束日期", example = DateUtil.TIMESTAMP_DEFAULT_TIME)
	private String endTime;
}
