package com.ft.br.service.impl;

import com.ft.br.service.GoodsService;
import com.ft.br.service.UserService;
import com.ft.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.ft.br.constant.StockLogTypeDetailEnum;
import com.ft.br.constant.StockLogTypeEnum;
import com.ft.br.dao.GoodsMapper;
import com.ft.br.dao.OrderMapper;
import com.ft.br.dao.StockLogMapper;
import com.ft.br.model.ao.stock.StockLogListAO;
import com.ft.br.model.bo.StockLogBO;
import com.ft.br.model.vo.OrderVO;
import com.ft.br.service.StockLogService;
import com.ft.dao.stock.model.GoodsDO;
import com.ft.dao.stock.model.OrderDO;
import com.ft.dao.stock.model.StockLogDO;
import com.ft.db.constant.DbConstant;
import com.ft.db.model.PageResult;
import com.ft.redis.base.ValueOperationsCache;
import com.ft.redis.lock.RedisLock;
import com.ft.util.StringUtil;
import com.ft.util.exception.FtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 出入库业务
 *
 * @author shichunyang
 */
@Service("com.ft.br.service.impl.StockLogServiceImpl")
public class StockLogServiceImpl implements StockLogService {

	@Autowired
	private StockLogMapper stockLogMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private GoodsMapper goodsMapper;

	@Autowired
	private GoodsService goodsService;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private ValueOperationsCache<String, String> valueOperationsCache;

	@Override
	public PageResult<StockLogBO> listByPage(StockLogListAO stockLogListAO) {
		int total = stockLogMapper.countPagination(stockLogListAO);
		PageResult<StockLogBO> pageResult = new PageResult<>();
		pageResult.setPage(stockLogListAO.getPage());
		pageResult.setLimit(stockLogListAO.getLimit());
		pageResult.setTotal(total);
		pageResult.setList(Lists.newArrayList());
		if (total <= 0) {
			return pageResult;
		}

		List<StockLogDO> stockLogDOs = stockLogMapper.listPagination(stockLogListAO);

		List<Integer> goodsIds = stockLogDOs.stream().map(StockLogDO::getGoodsId).distinct().collect(Collectors.toList());
		Map<Integer, String> goodsNameMap = goodsService.listGoodsNamesByIds(goodsIds);

		List<Integer> operatorIds = stockLogDOs.stream().map(StockLogDO::getOperator).distinct().collect(Collectors.toList());
		Map<Integer, String> userNameMap = userService.listUserNamesByIds(operatorIds);

		List<StockLogBO> list = stockLogDOs.stream().map(stockLogDO -> {
			StockLogBO stockLogBO = ObjectUtil.copy(stockLogDO, StockLogBO.class);
			if (stockLogBO == null) {
				stockLogBO = new StockLogBO();
			}

			stockLogBO.setOperatorName(userNameMap.get(stockLogDO.getOperator()));

			StockLogTypeEnum stockLogTypeEnum = StockLogTypeEnum.getByType(stockLogDO.getType());
			if (stockLogTypeEnum != null) {
				stockLogBO.setTypeCH(stockLogTypeEnum.getMessage());
			}

			StockLogTypeDetailEnum stockLogTypeDetailEnum = StockLogTypeDetailEnum.getByTypeDetail(stockLogDO.getTypeDetail());
			if (stockLogTypeDetailEnum != null) {
				stockLogBO.setTypeDetailCH(stockLogTypeDetailEnum.getMessage());
			}

			stockLogBO.setGoodsName(goodsNameMap.get(stockLogDO.getGoodsId()));

			return stockLogBO;
		}).collect(Collectors.toList());
		pageResult.setList(list);

		return pageResult;
	}

	/**
	 * 出入库操作
	 *
	 * @param stockLogDO 仓库对象
	 * @return true:操作成功
	 */
	@Transactional(value = DbConstant.DB_CONSIGN + DbConstant.TRAN_SACTION_MANAGER, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public boolean storage(StockLogDO stockLogDO) {
		// 出入库类型
		Integer type = stockLogDO.getType();
		// 商品id
		int goodsId = stockLogDO.getGoodsId();
		// 出入库商品数量
		int goodsNumber = stockLogDO.getGoodsNumber();

		if (goodsNumber < 1) {
			FtException.throwException("仓库操作失败, 商品数量不能小于1");
		}

		GoodsDO goodsDO = goodsMapper.selectByPrimaryKey(goodsId);
		if (goodsDO == null) {
			FtException.throwException("商品不存在，操作仓库失败");
		}

		String defaultOrderId = "0";
		if (!stockLogDO.getOrderId().equals(defaultOrderId)) {
			OrderDO orderDO = orderMapper.selectByPrimaryKey(stockLogDO.getOrderId());
			OrderVO order = new OrderVO();
			if (order == null) {
				FtException.throwException("仓库操作失败, 订单不存在");
			}
		}

		if (type == StockLogTypeEnum.IN.getType().shortValue()) {
			// 入库操作
			goodsMapper.updateNumber(goodsId, goodsNumber);
			stockLogDO.setTypeDetail(StockLogTypeDetailEnum.IN_PERSON.getTypeDetail());
			stockLogMapper.insertSelective(stockLogDO);
		} else if (type == StockLogTypeEnum.OUT.getType().shortValue()) {
			// 出库操作
			if (goodsNumber > goodsDO.getNumber()) {
				FtException.throwException("出库失败, 商品库存数量不足");
			}

			String lockKey = StringUtil.append(StringUtil.REDIS_SPLIT, "storage", "goods", goodsId + "");
			RedisLock redisLock = new RedisLock(valueOperationsCache);
			redisLock.lock(lockKey, 10_000L);
			goodsDO = goodsMapper.selectByPrimaryKey(goodsId);
			if (goodsNumber <= goodsDO.getNumber()) {
				goodsMapper.updateNumber(goodsId, goodsNumber * -1);
				stockLogDO.setTypeDetail(StockLogTypeDetailEnum.OUT_PERSON.getTypeDetail());
				stockLogMapper.insertSelective(stockLogDO);

				redisLock.unlock(lockKey);
			} else {
				redisLock.unlock(lockKey);
				return false;
			}
		} else {
			FtException.throwException("未知操作仓库类型");
		}
		return true;
	}
}
