package com.ft.br.service;

import com.ft.br.model.ao.goods.GoodsAddAO;
import com.ft.br.model.ao.goods.GoodsGetAO;
import com.ft.dao.stock.model.GoodsDO;

/**
 * GoodsService
 *
 * @author shichunyang
 */
public interface GoodsService {

	/**
	 * 添加商品信息
	 *
	 * @param goodsAddAO 商品信息
	 * @return true 添加成功
	 */
	boolean add(GoodsAddAO goodsAddAO);

	/**
	 * 根据主键查询商品信息
	 *
	 * @param goodsGetAO 商品主键
	 * @return 商品信息
	 */
	GoodsDO get(GoodsGetAO goodsGetAO);
}
