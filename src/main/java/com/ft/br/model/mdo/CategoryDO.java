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
	 * 主键
	 */
	private Integer id;

	/**
	 * 分类名称
	 */
	private String name;
}
