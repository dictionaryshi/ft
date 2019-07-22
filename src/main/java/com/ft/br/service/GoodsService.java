package com.ft.br.service;

import com.ft.br.model.ao.goods.GoodsAddAO;

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
}
