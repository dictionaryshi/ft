package com.ft.br.service;

import com.ft.br.dao.CategoryMapper;
import com.ft.br.dao.GoodsMapper;
import com.ft.br.model.dto.GoodsDTO;
import com.ft.br.model.vo.GoodsVO;
import com.ft.dao.stock.model.CategoryDO;
import com.ft.dao.stock.model.GoodsDO;
import com.ft.db.annotation.UseMaster;
import com.ft.db.model.PageParam;
import com.ft.db.model.PageResult;
import com.ft.util.exception.FtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品业务
 *
 * @author shichunyang
 */
@Service
@Slf4j
public class GoodsService {

	@Autowired
	private GoodsMapper goodsMapper;

	@Autowired
	private CategoryMapper categoryMapper;

	/**
	 * 根据商品id查询商品信息
	 *
	 * @param id 商品id
	 * @return 商品信息
	 */
	public GoodsDO get(int id) {
		return goodsMapper.getGoodsById(id);
	}

	/**
	 * 根据id修改商品信息
	 *
	 * @param id         主键
	 * @param name       商品名称
	 * @param categoryId 分类id
	 * @return true:修改成功
	 */
	@UseMaster
	public boolean update(int id, String name, int categoryId) {

		GoodsDO goodsDO = goodsMapper.getGoodsById(id);
		if (goodsDO == null) {
			FtException.throwException("商品不存在,无法修改");
		}

		CategoryDO categoryDO = categoryMapper.getCategoryById(categoryId);
		if (categoryDO == null) {
			FtException.throwException("商品分类不存在,无法修改");
		}

		GoodsDO dbGoods = goodsMapper.getGoodsByName(name);
		if (dbGoods != null && dbGoods.getCategory() == categoryId) {
			FtException.throwException("商品已经存在");
		}

		goodsDO = new GoodsDO();
		goodsDO.setId(id);
		goodsDO.setName(name);
		goodsDO.setCategory(categoryId);

		return goodsMapper.update(goodsDO) == 1;
	}

	/**
	 * 添加商品
	 *
	 * @param name       商品名称
	 * @param categoryId 分类id
	 * @return true:添加成功
	 */
	@UseMaster
	public boolean add(String name, int categoryId) {

		CategoryDO categoryDO = categoryMapper.getCategoryById(categoryId);
		if (categoryDO == null) {
			FtException.throwException("商品分类不存在");
		}

		if (goodsMapper.getGoodsByName(name) != null) {
			FtException.throwException("商品已经存在");
		}

		GoodsDO goodsDO = new GoodsDO();
		goodsDO.setName(name);
		goodsDO.setCategory(categoryId);

		return goodsMapper.insert(goodsDO) == 1;
	}

	/**
	 * 分页查询商品信息
	 *
	 * @param categoryId 分类id
	 * @param pageParam  分页工具类
	 * @return 商品信息
	 */
	public PageResult<GoodsVO> list(int categoryId, PageParam pageParam) {
		PageResult<GoodsVO> pageResult = new PageResult<>();
		pageResult.setPage(pageParam.getPage());
		pageResult.setLimit(pageParam.getLimit());

		GoodsDTO goodsDTO = new GoodsDTO();
		if (categoryId != 0) {
			goodsDTO.setCategory(categoryId);
		}

		int count = goodsMapper.countPaging(goodsDTO);
		if (count == 0) {
			pageResult.setTotal(0);
			pageResult.setList(new ArrayList<>());
			return pageResult;
		}

		goodsDTO.setStartRow(pageParam.getStartRowNumber());
		goodsDTO.setPageSize(pageParam.getLimit());

		List<GoodsVO> goods = goodsMapper.listPaging(goodsDTO);
		this.format(goods);

		pageResult.setTotal(count);
		pageResult.setList(goods);

		return pageResult;
	}

	public void format(List<GoodsVO> goods) {
		if (goods.isEmpty()) {
			return;
		}

		goods.forEach(goodsVO -> {
			int category = goodsVO.getCategory();
			CategoryDO categoryDO = categoryMapper.getCategoryById(category);
			if (categoryDO != null) {
				goodsVO.setCategoryName(categoryDO.getName());
			}
		});
	}
}
