package com.ft.model.dto;

import com.ft.model.mdo.GoodsDO;
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
