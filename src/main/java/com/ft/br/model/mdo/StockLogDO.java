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
	private Short type;
	/**
	 * 详细操作类型
	 */
	private Short typeDetail;
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
	private String orderId;
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
/*
    CREATE TABLE `stock_log` (
      `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
      `operator` int(11) NOT NULL DEFAULT '0' COMMENT '操作人',
      `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '类型',
      `type_detail` tinyint(4) NOT NULL DEFAULT '0' COMMENT '详细操作类型',
      `goods_id` int(11) NOT NULL DEFAULT '0' COMMENT '商品id',
      `goods_number` int(11) NOT NULL DEFAULT '0' COMMENT '商品数量',
      `order_id` varchar(32) NOT NULL DEFAULT '0' COMMENT '订单id',
      `remark` varchar(1000) NOT NULL DEFAULT '' COMMENT '备注信息',
      `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */