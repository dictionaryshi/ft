package com.ft.br.model.dto;

import com.ft.dao.stock.model.GoodsDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品DTO
 *
 * @author shichunyang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsDTO extends GoodsDO {
	private Integer startRow;
	private Integer pageSize;
}
