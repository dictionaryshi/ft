package com.ft.br.model.ao;

import com.ft.db.model.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * StockLogListAO
 *
 * @author shichunyang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StockLogListAO extends PageParam {
	/**
	 * 类型
	 */
	private Integer type;

	/**
	 * 商品id
	 */
	private Integer goodsId;

	/**
	 * 订单id
	 */
	private Long orderId;

	/**
	 * 开始日期
	 */
	private String startTime;

	/**
	 * 结束日期
	 */
	private String endTime;
}
