package com.ft.br.model.vo;

import com.ft.br.model.mdo.GoodsDO;
import com.ft.model.mdo.GoodsDO;
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
