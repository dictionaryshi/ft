package com.ft.br.model.mdo;

import lombok.Data;

import java.util.Date;

/**
 * 订单模型
 *
 * @author shichunyang
 */
@Data
public class OrderDO {

	/**
	 * 订单号
	 */
	private String id;
	/**
	 * 操作人
	 */
	private Long operator;
	/**
	 * 订单状态{@link OrderTypeEnum}
	 */
	private Short status;
	/**
	 * 客户姓名
	 */
	private String username;
	/**
	 * 客户电话
	 */
	private String phone;
	/**
	 * 送货地址
	 */
	private String address;
	/**
	 * 订单总金额
	 */
	private Double totalAmount;
	/**
	 * 订单备注
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
    CREATE TABLE `order` (
      `id` char(17) NOT NULL DEFAULT '0' COMMENT '主键(订单号)',
      `operator` int(11) NOT NULL DEFAULT '0' COMMENT '操作人',
      `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '订单状态',
      `username` varchar(50) NOT NULL DEFAULT '' COMMENT '客户姓名',
      `phone` varchar(25) NOT NULL DEFAULT '' COMMENT '客户电话号',
      `address` varchar(1000) NOT NULL DEFAULT '' COMMENT '送货地址',
      `total_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '订单总金额',
      `remark` varchar(1000) NOT NULL DEFAULT '' COMMENT '订单备注',
      `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */