package com.ft.br.model.mdo;

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
