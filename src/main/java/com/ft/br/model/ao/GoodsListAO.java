package com.ft.br.model.ao;

import com.ft.db.model.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * GoodsListAO
 *
 * @author shichunyang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsListAO extends PageParam {
	/**
	 * 分类
	 */
	private Integer category;
}
