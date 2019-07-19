package com.ft.br.model.mdo;

import lombok.Data;

import java.util.Date;

/**
 * 出入库日志DO
 *
 * @author shichunyang
 */
@Data
public class StockLogDO {

	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 操作人
	 */
	private Long operator;
	/**
	 * 类型
	 */
	private Integer type;
	/**
	 * 详细操作类型
	 */
	private Integer typeDetail;
	/**
	 * 商品id
	 */
	private Long goodsId;
	/**
	 * 商品数量
	 */
	private Integer goodsNumber;
	/**
	 * 订单号
	 */
	private Long orderId;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建时间
	 */
	private Date createdAt;
	/**
	 * 修改时间
	 */
	private Date updatedAt;
}
