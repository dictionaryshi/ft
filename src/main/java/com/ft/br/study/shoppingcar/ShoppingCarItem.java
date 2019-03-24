package com.ft.br.study.shoppingcar;

import com.ft.util.BigDecimalUtil;
import lombok.Data;

/**
 * ShoppingCarItem
 *
 * @author shichunyang
 */
@Data
public class ShoppingCarItem {
	/**
	 * 商品主键
	 */
	private String id;
	/**
	 * 商品名称
	 */
	private String name;
	/**
	 * 商品价格
	 */
	private Double price;
	/**
	 * 商品数量
	 */
	private Integer count;

	/**
	 * 小记
	 */
	public double getSubtotal() {
		return BigDecimalUtil.multiply(price + "", count + "").doubleValue();
	}
}
