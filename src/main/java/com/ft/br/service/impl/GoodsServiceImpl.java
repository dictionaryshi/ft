package com.ft.br.service.impl;

import com.ft.br.constant.RedisKey;
import com.ft.br.dao.CategoryMapper;
import com.ft.br.dao.GoodsMapper;
import com.ft.br.model.ao.GoodsListAO;
import com.ft.br.model.ao.goods.GoodsAddAO;
import com.ft.br.model.vo.GoodsVO;
import com.ft.br.service.CategoryService;
import com.ft.br.service.GoodsService;
import com.ft.dao.stock.model.CategoryDO;
import com.ft.dao.stock.model.GoodsDO;
import com.ft.db.annotation.UseMaster;
import com.ft.db.model.PageParam;
import com.ft.db.model.PageResult;
import com.ft.redis.lock.RedisLock;
import com.ft.redis.util.RedisUtil;
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
@Slf4j
@Service("com.ft.br.service.impl.GoodsServiceImpl")
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private GoodsMapper goodsMapper;

	@Autowired
	private CategoryMapper categoryMapper;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private RedisLock redisLock;

	@Override
	public boolean add(GoodsAddAO goodsAddAO) {
		int categoryId = goodsAddAO.getCategoryId();
		String name = goodsAddAO.getName();

		CategoryDO categoryDO = categoryMapper.selectByPrimaryKey(categoryId);
		if (categoryDO == null) {
			FtException.throwException("商品分类不存在");
		}

		String lockKey = RedisUtil.getRedisKey(RedisKey.REDIS_GOODS_ADD_LOCK, categoryId + "_" + name);
		try {
			redisLock.lock(lockKey, 10_000L);
			if (goodsMapper.getGoodsByName(categoryId, name) != null) {
				FtException.throwException("商品已经存在");
			}

			GoodsDO goodsDO = new GoodsDO();
			goodsDO.setName(name);
			goodsDO.setCategory(categoryId);

			return goodsMapper.insertSelective(goodsDO) == 1;
		} finally {
			redisLock.unlock(lockKey);
		}
	}

	/**
	 * 根据商品id查询商品信息
	 *
	 * @param id 商品id
	 * @return 商品信息
	 */
	public GoodsDO get(int id) {
		return goodsMapper.selectByPrimaryKey(id);
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

		GoodsDO goodsDO = goodsMapper.selectByPrimaryKey(id);
		if (goodsDO == null) {
			FtException.throwException("商品不存在,无法修改");
		}

		CategoryDO categoryDO = categoryMapper.selectByPrimaryKey(categoryId);
		if (categoryDO == null) {
			FtException.throwException("商品分类不存在,无法修改");
		}

		GoodsDO dbGoods = goodsMapper.getGoodsByName(categoryId, name);
		if (dbGoods != null) {
			FtException.throwException("商品已经存在");
		}

		goodsDO = new GoodsDO();
		goodsDO.setId(id);
		goodsDO.setName(name);
		goodsDO.setCategory(categoryId);

		return goodsMapper.updateByPrimaryKeySelective(goodsDO) == 1;
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

		GoodsListAO goodsDTO = new GoodsListAO();
		if (categoryId != 0) {
			goodsDTO.setCategory(categoryId);
		}

		int count = goodsMapper.countPaging(goodsDTO);
		if (count == 0) {
			pageResult.setTotal(0);
			pageResult.setList(new ArrayList<>());
			return pageResult;
		}

		goodsDTO.setStartRowNumber(pageParam.getStartRowNumber());
		goodsDTO.setLimit(pageParam.getLimit());

		List<GoodsDO> goodsDOS = goodsMapper.listPaging(goodsDTO);
		List<GoodsVO> goods = new ArrayList<>();
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
			CategoryDO categoryDO = categoryMapper.selectByPrimaryKey(category);
			if (categoryDO != null) {
				goodsVO.setCategoryName(categoryDO.getName());
			}
		});
	}
}
