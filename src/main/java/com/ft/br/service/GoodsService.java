package com.ft.br.service;

import com.ft.br.model.ao.goods.GoodsListAO;
import com.ft.br.model.ao.goods.GoodsAddAO;
import com.ft.br.model.ao.goods.GoodsGetAO;
import com.ft.br.model.ao.goods.GoodsUpdateAO;
import com.ft.br.model.bo.GoodsBO;
import com.ft.dao.stock.model.GoodsDO;
import com.ft.db.model.PageResult;

import java.util.List;
import java.util.Map;

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
	GoodsBO get(GoodsGetAO goodsGetAO);

	/**
	 * 根据主键查询商品信息
	 *
	 * @param id 商品主键
	 * @return 商品信息
	 */
	GoodsDO get(int id);

	/**
	 * 根据分类查询所有商品信息
	 *
	 * @param categoryId 分类id
	 * @return 所有商品信息
	 */
	List<GoodsBO> listByCategoryId(int categoryId);

	/**
	 * 修改商品信息
	 *
	 * @param goodsUpdateAO 商品信息
	 * @return true 修改成功
	 */
	boolean update(GoodsUpdateAO goodsUpdateAO);

	/**
	 * 分页查询商品信息
	 *
	 * @param goodsListAO 分页条件
	 * @return 商品信息
	 */
	PageResult<GoodsBO> listByPage(GoodsListAO goodsListAO);

	/**
	 * 批量获取商品名称
	 *
	 * @param ids 商品id集合
	 * @return 商品名称集合
	 */
	Map<Integer, String> listGoodsNamesByIds(List<Integer> ids);

	/**
	 * 获取库存
	 *
	 * @param goodsId 商品id
	 * @return 商品库存数量
	 */
	Integer getStock(int goodsId);
}
