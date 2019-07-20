package com.ft.br.model.ao;

import com.ft.db.model.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderListAO extends PageParam {
	/**
	 * 主键(订单号)
	 */
	private Long id;

	/**
	 * 订单状态
	 */
	private Integer status;

	/**
	 * 开始日期
	 */
	private String startTime;

	/**
	 * 结束日期
	 */
	private String endTime;
}
