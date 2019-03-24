package com.ft.br.model.vo;

import com.ft.br.model.mdo.ItemDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单项VO
 *
 * @author shichunyang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ItemVO extends ItemDO {
	/**
	 * 商品名称
	 */
	private String goodsName;
}
