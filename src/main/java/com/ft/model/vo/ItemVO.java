package com.ft.model.vo;

import com.ft.model.mdo.ItemDO;
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
