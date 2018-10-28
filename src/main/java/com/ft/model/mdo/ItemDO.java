package com.ft.model.mdo;

import lombok.Data;

import java.util.Date;

/**
 * 订单条目
 *
 * @author shichunyang
 */
@Data
public class ItemDO {

	private Long id;
	private String orderId;
	private Long goodsId;
	private Integer goodsNumber;
	private Date createdAt;
	private Date updatedAt;
}
/*
    CREATE TABLE `item` (
      `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
      `order_id` char(17) NOT NULL DEFAULT '0' COMMENT '订单id',
      `goods_id` int(11) NOT NULL DEFAULT '0' COMMENT '商品id',
      `goods_number` int(11) NOT NULL DEFAULT '0' COMMENT '商品数量',
      `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */