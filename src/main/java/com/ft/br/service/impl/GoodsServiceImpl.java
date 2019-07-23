package com.ft.br.service.impl;

import com.ft.br.constant.RedisKey;
import com.ft.br.dao.CategoryMapper;
import com.ft.br.dao.GoodsMapper;
import com.ft.br.model.ao.goods.GoodsListAO;
import com.ft.br.model.ao.goods.GoodsAddAO;
import com.ft.br.model.ao.goods.GoodsGetAO;
import com.ft.br.model.ao.goods.GoodsUpdateAO;
import com.ft.br.model.bo.CategoryBO;
import com.ft.br.model.bo.GoodsBO;
import com.ft.br.service.CategoryService;
import com.ft.br.service.GoodsService;
import com.ft.dao.stock.model.CategoryDO;
import com.ft.dao.stock.model.GoodsDO;
import com.ft.db.annotation.UseMaster;
import com.ft.db.model.PageResult;
import com.ft.redis.lock.RedisLock;
import com.ft.redis.util.RedisUtil;
import com.ft.util.ObjectUtil;
import com.ft.util.StringUtil;
import com.ft.util.exception.FtException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	@UseMaster
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

	@Override
	public GoodsBO get(GoodsGetAO goodsGetAO) {
		int id = goodsGetAO.getId();
		GoodsDO goodsDO = this.get(id);
		if (goodsDO == null) {
			return null;
		}

		GoodsBO goodsBO = new GoodsBO();
		goodsBO.setId(goodsDO.getId());
		goodsBO.setGoodsName(goodsDO.getName());
		goodsBO.setStockNumber(goodsDO.getNumber());
		goodsBO.setCategoryId(goodsDO.getCategory());

		CategoryBO categoryBO = categoryService.getById(goodsDO.getCategory());
		if (categoryBO != null) {
			goodsBO.setCategoryName(categoryBO.getName());
		}

		return goodsBO;
	}

	@Override
	public GoodsDO get(int id) {
		return goodsMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<GoodsBO> listByCategoryId(int categoryId) {
		Map<Integer, GoodsDO> goodsMap = goodsMapper.selectByCategory(categoryId);

		List<Integer> categoryIds = goodsMap.values().stream().map(GoodsDO::getCategory).distinct().collect(Collectors.toList());
		Map<Integer, String> categoryNameMap = categoryService.listCategoryNameByIds(categoryIds);

		return goodsMap.values().stream().map(goodsDO -> this.goodsDO2GoodsBO(goodsDO, categoryNameMap)).collect(Collectors.toList());
	}

	@UseMaster
	@Override
	public boolean update(GoodsUpdateAO goodsUpdateAO) {
		int id = goodsUpdateAO.getId();

		GoodsDO goodsDO = goodsMapper.selectByPrimaryKey(id);
		if (goodsDO == null) {
			FtException.throwException("商品不存在");
		}

		int categoryId = goodsUpdateAO.getCategoryId();
		CategoryBO categoryBO = categoryService.getById(categoryId);
		if (categoryBO == null) {
			FtException.throwException("商品分类不存在");
		}

		String lockKey = RedisUtil.getRedisKey(RedisKey.REDIS_GOODS_UPDATE_LOCK, id + "");

		try {
			redisLock.lock(lockKey, 10_000L);

			String name = goodsUpdateAO.getName();

			goodsDO = goodsMapper.getGoodsByName(categoryId, name);
			if (goodsDO != null) {
				FtException.throwException("商品分类关系已存在");
			}

			GoodsDO update = new GoodsDO();
			update.setId(id);
			update.setName(name);
			update.setCategory(categoryId);

			return goodsMapper.updateByPrimaryKeySelective(update) == 1;
		} finally {
			redisLock.unlock(lockKey);
		}
	}

	@Override
	public PageResult<GoodsBO> listByPage(GoodsListAO goodsListAO) {
		int total = goodsMapper.countPaging(goodsListAO);

		PageResult<GoodsBO> pageResult = new PageResult<>();
		pageResult.setPage(goodsListAO.getPage());
		pageResult.setLimit(goodsListAO.getLimit());
		pageResult.setTotal(total);
		pageResult.setList(Lists.newArrayList());
		if (total <= 0) {
			return pageResult;
		}

		List<GoodsDO> goodsDOs = goodsMapper.listPaging(goodsListAO);

		List<Integer> categoryIds = goodsDOs.stream().map(GoodsDO::getCategory).distinct().collect(Collectors.toList());
		Map<Integer, String> categoryNameMap = categoryService.listCategoryNameByIds(categoryIds);

		List<GoodsBO> list = goodsDOs.stream().map(goodsDO -> this.goodsDO2GoodsBO(goodsDO, categoryNameMap)).collect(Collectors.toList());
		pageResult.setList(list);

		return pageResult;
	}

	private GoodsBO goodsDO2GoodsBO(GoodsDO goodsDO, Map<Integer, String> categoryNameMap) {
		GoodsBO goodsBO = new GoodsBO();
		goodsBO.setId(goodsDO.getId());
		goodsBO.setGoodsName(goodsDO.getName());
		goodsBO.setStockNumber(goodsDO.getNumber());
		goodsBO.setCategoryId(goodsDO.getCategory());

		String categoryName = categoryNameMap.get(goodsDO.getCategory());
		goodsBO.setCategoryName(categoryName);

		return goodsBO;
	}

	@Override
	public Map<Integer, String> listGoodsNamesByIds(List<Integer> ids) {
		Map<Integer, String> resultMap = new HashMap<>(16);

		if (ObjectUtil.isEmpty(ids)) {
			return resultMap;
		}

		String idStr = StringUtil.join(ids, ",");
		Map<Integer, GoodsDO> goodsDOMap = goodsMapper.selectByIds(idStr);
		goodsDOMap.forEach((id, goodsDO) -> resultMap.put(id, goodsDO.getName()));

		return resultMap;
	}
}
