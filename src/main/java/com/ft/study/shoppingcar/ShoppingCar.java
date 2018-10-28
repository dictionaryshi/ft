package com.ft.study.shoppingcar;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ShoppingCar {

	/**
	 * 购物车条目
	 */
	private Map<String, ShoppingCarItem> shoppingCarItemMap = new LinkedHashMap<>();

	/**
	 * 合计
	 */
	public double getTotal() {
		BigDecimal total = new BigDecimal("0");

		for (ShoppingCarItem shoppingCarItem : shoppingCarItemMap.values()) {

			BigDecimal subTotal = new BigDecimal(shoppingCarItem.getSubtotal() + "");
			total = total.add(subTotal);
		}

		return total.doubleValue();
	}

	/**
	 * 向购物车添加条目
	 */
	public void add(ShoppingCarItem shoppingCarItem) {

		String id = shoppingCarItem.getId();
		if (shoppingCarItemMap.containsKey(id)) {

			ShoppingCarItem old = shoppingCarItemMap.get(id);
			int oldCount = old.getCount();
			int newCount = shoppingCarItem.getCount() + oldCount;

			shoppingCarItem.setCount(newCount);
			shoppingCarItemMap.put(id, shoppingCarItem);

		} else {
			shoppingCarItemMap.put(id, shoppingCarItem);
		}
	}

	/**
	 * 清空购物车条目
	 */
	public void clear() {
		shoppingCarItemMap.clear();
	}

	/**
	 * 删除购物车条目(根据商品id删除)
	 */
	public void delete(String bid) {
		shoppingCarItemMap.remove(bid);
	}

	/**
	 * 获取我的购物车中所有条目
	 */
	public Collection<ShoppingCarItem> getShoppingCarItems() {
		return shoppingCarItemMap.values();
	}
}
