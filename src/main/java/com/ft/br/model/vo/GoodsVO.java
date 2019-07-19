package com.ft.br.model.vo;

import com.ft.dao.stock.model.GoodsDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品VO
 *
 * @author shichunyang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsVO extends GoodsDO {

	private String categoryName;
}
