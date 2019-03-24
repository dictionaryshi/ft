package com.ft.br.model.mdo;

import lombok.Data;

import java.io.Serializable;

/**
 * 分类DO
 *
 * @author shichunyang
 */
@Data
public class CategoryDO implements Serializable {

	/**
	 * 分类主键
	 */
	private Short id;
	/**
	 * 分类名称
	 */
	private String name;
}
/*
    CREATE TABLE `category` (
      `id` smallint(6) NOT NULL AUTO_INCREMENT COMMENT '主键',
      `name` varchar(50) NOT NULL DEFAULT '' COMMENT '分类名称',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */