package com.ft.model.mdo;

import lombok.Data;

import java.util.Date;

/**
 * 商品DO
 *
 * @author shichunyang
 */
@Data
public class GoodsDO {

	/**
	 * 主键
	 */
	private Long id;

	/**
	 * 商品名称
	 */
	private String name;

	/**
	 * 商品库存数量
	 */
	private Integer number;

	/**
	 * 商品分类
	 */
	private Short category;

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
    CREATE TABLE `goods` (
      `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
      `name` varchar(50) NOT NULL DEFAULT '' COMMENT '商品名称',
      `number` int(11) NOT NULL DEFAULT '0' COMMENT '库存数量',
      `category` smallint(6) NOT NULL DEFAULT '0' COMMENT '分类',
      `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */