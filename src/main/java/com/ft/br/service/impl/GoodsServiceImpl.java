package com.ft.br.service.impl;

import com.ft.br.constant.RedisKey;
import com.ft.br.dao.CategoryMapper;
import com.ft.br.dao.GoodsMapper;
import com.ft.br.model.ao.GoodsListAO;
import com.ft.br.model.ao.goods.GoodsAddAO;
import com.ft.br.model.ao.goods.GoodsGetAO;
import com.ft.br.model.ao.goods.GoodsUpdateAO;
import com.ft.br.model.bo.CategoryBO;
import com.ft.br.model.bo.GoodsBO;
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

		return goodsMap.values().stream().map(goodsDO -> {
			GoodsBO goodsBO = new GoodsBO();
			goodsBO.setId(goodsDO.getId());
			goodsBO.setGoodsName(goodsDO.getName());
			goodsBO.setStockNumber(goodsDO.getNumber());
			goodsBO.setCategoryId(goodsDO.getCategory());

			String categoryName = categoryNameMap.get(goodsDO.getCategory());
			goodsBO.setCategoryName(categoryName);

			return goodsBO;
		}).collect(Collectors.toList());
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
