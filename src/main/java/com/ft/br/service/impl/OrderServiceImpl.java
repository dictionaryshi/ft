package com.ft.br.service.impl;

import com.ft.br.constant.OrderStatusEnum;
import com.ft.br.constant.RedisKey;
import com.ft.br.constant.StockLogTypeDetailEnum;
import com.ft.br.constant.StockLogTypeEnum;
import com.ft.br.dao.*;
import com.ft.br.model.ao.item.ItemAddAO;
import com.ft.br.model.ao.item.ItemUpdateAO;
import com.ft.br.model.ao.order.OrderAddUpdateAO;
import com.ft.br.model.ao.order.OrderGetAO;
import com.ft.br.model.ao.order.OrderListAO;
import com.ft.br.model.bo.ItemBO;
import com.ft.br.model.bo.OrderBO;
import com.ft.br.model.vo.ItemVO;
import com.ft.br.model.vo.OrderVO;
import com.ft.br.service.GoodsService;
import com.ft.br.service.OrderService;
import com.ft.dao.stock.model.*;
import com.ft.db.annotation.UseMaster;
import com.ft.db.constant.DbConstant;
import com.ft.db.model.PageResult;
import com.ft.redis.base.ValueOperationsCache;
import com.ft.redis.lock.RedisLock;
import com.ft.redis.util.RedisUtil;
import com.ft.util.*;
import com.ft.util.exception.FtException;
import com.ft.util.model.LogAO;
import com.ft.util.model.LogBO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 订单业务类
 *
 * @author shichunyang
 */
@Slf4j
@Service("com.ft.br.service.impl.OrderServiceImpl")
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private ItemMapper itemMapper;

	@Autowired
	private GoodsMapper goodsMapper;

	@Autowired
	private StockLogMapper stockLogMapper;

	@Autowired
	private ValueOperationsCache<String, String> valueOperationsCache;

	@Autowired
	private RedisLock redisLock;

	@Autowired
	private GoodsService goodsService;

	@UseMaster
	@Override
	public boolean createOrder(OrderAddUpdateAO orderAddAO) {
		Long orderId = orderAddAO.getId();
		OrderDO dbOrder = orderMapper.selectByPrimaryKey(orderId);
		if (dbOrder != null) {
			FtException.throwException("订单已存在");
		}

		OrderDO orderDO = ObjectUtil.copy(orderAddAO, OrderDO.class);
		if (orderDO == null) {
			FtException.throwException("订单信息copy失败",
					LogAO.build("orderAddAO", JsonUtil.object2Json(orderAddAO)));
		}

		orderDO.setStatus(OrderStatusEnum.WAIT_TO_CONFIRMED.getStatus());

		orderMapper.insertSelective(orderDO);

		return Boolean.TRUE;
	}

	@UseMaster
	@Override
	public boolean updateOrder(OrderAddUpdateAO orderAddUpdateAO) {
		Long orderId = orderAddUpdateAO.getId();
		OrderDO dbOrder = orderMapper.selectByPrimaryKey(orderId);
		if (dbOrder == null) {
			FtException.throwException("订单不存在");
		}

		OrderDO update = ObjectUtil.copy(orderAddUpdateAO, OrderDO.class);
		if (update == null) {
			FtException.throwException("订单信息copy失败",
					LogAO.build("orderAddUpdateAO", JsonUtil.object2Json(orderAddUpdateAO)));
		}

		orderMapper.updateByPrimaryKeySelective(update);

		return Boolean.TRUE;
	}

	@Override
	public OrderBO getOrderById(OrderGetAO orderGetAO) {
		OrderDO orderDO = orderMapper.selectByPrimaryKey(orderGetAO.getId());
		if (orderDO == null) {
			return new OrderBO();
		}

		return this.orderDO2OrderBO(orderDO);
	}

	private OrderBO orderDO2OrderBO(OrderDO orderDO) {
		OrderBO orderBO = new OrderBO();
		orderBO.setId(orderDO.getId());

		UserDO userDO = userMapper.selectByPrimaryKey(orderDO.getOperator());
		if (userDO != null) {
			orderBO.setOperatorName(userDO.getUsername());
		}

		orderBO.setStatus(orderDO.getStatus());

		OrderStatusEnum orderStatusEnum = OrderStatusEnum.getByStatus(orderDO.getStatus());
		if (orderStatusEnum != null) {
			orderBO.setStatusCH(orderStatusEnum.getMessage());
		}

		orderBO.setUsername(orderDO.getUsername());
		orderBO.setPhone(orderDO.getPhone());
		orderBO.setAddress(orderDO.getAddress());
		orderBO.setTotalAmount(orderDO.getTotalAmount());
		orderBO.setRemark(orderDO.getRemark());

		if (orderDO.getConfirmTime() > 0) {
			orderBO.setConfirmTimeCH(DateUtil.date2Str(new Date(orderDO.getConfirmTime()), DateUtil.DEFAULT_DATE_FORMAT));
		}

		if (orderDO.getFinalOperateTime() > 0) {
			orderBO.setFinalOperateTimeCH(DateUtil.date2Str(new Date(orderDO.getFinalOperateTime()), DateUtil.DEFAULT_DATE_FORMAT));
		}

		orderBO.setCreatedAtCH(DateUtil.date2Str(orderDO.getCreatedAt(), DateUtil.DEFAULT_DATE_FORMAT));

		return orderBO;
	}

	@Override
	public PageResult<OrderBO> listByPage(OrderListAO orderListAO) {
		int total = orderMapper.countPagination(orderListAO);

		PageResult<OrderBO> pageResult = new PageResult<>();
		pageResult.setPage(orderListAO.getPage());
		pageResult.setLimit(orderListAO.getLimit());
		pageResult.setTotal(total);
		pageResult.setList(Lists.newArrayList());

		if (total <= 0) {
			return pageResult;
		}

		List<OrderDO> orderDOS = orderMapper.listPagination(orderListAO);
		List<OrderBO> list = orderDOS.stream().map(this::orderDO2OrderBO).collect(Collectors.toList());
		pageResult.setList(list);

		return pageResult;
	}

	@UseMaster
	@Override
	public boolean addItem(ItemAddAO itemAO) {
		long orderId = itemAO.getOrderId();
		int goodsId = itemAO.getGoodsId();
		int goodsNumber = itemAO.getGoodsNumber();

		OrderDO orderDO = orderMapper.selectByPrimaryKey(orderId);
		if (orderDO == null) {
			FtException.throwException("订单不存在");
		}

		String orderLockKey = RedisUtil.getRedisKey(RedisKey.REDIS_ORDER_UPDATE_LOCK, orderDO.getId() + "");

		try {
			redisLock.lock(orderLockKey, 10_000L);

			orderDO = orderMapper.selectByPrimaryKey(orderId);
			if (!ObjectUtil.equals(orderDO.getStatus(), OrderStatusEnum.WAIT_TO_CONFIRMED.getStatus())) {
				FtException.throwException("只有待确认订单才可添加订单项");
			}

			GoodsDO goodsDO = goodsMapper.selectByPrimaryKey(goodsId);
			if (goodsDO == null) {
				FtException.throwException("商品不存在");
			}

			String lockKey = RedisUtil.getRedisKey(RedisKey.REDIS_ITEM_ADD_LOCK, orderId + "_" + goodsId);

			try {
				redisLock.lock(lockKey, 10_000L);

				ItemDO itemDO = itemMapper.selectByOrderIdAndGoodsId(orderId, goodsId);
				if (itemDO != null) {
					FtException.throwException("请不要重复添加商品");
				}

				itemDO = new ItemDO();
				itemDO.setOrderId(orderId);
				itemDO.setGoodsId(goodsId);
				itemDO.setGoodsNumber(goodsNumber);

				return itemMapper.insertSelective(itemDO) == 1;
			} finally {
				redisLock.unlock(lockKey);
			}
		} finally {
			redisLock.unlock(orderLockKey);
		}
	}

	@Override
	public List<ItemBO> listItems(long orderId) {
		List<ItemDO> itemDOS = itemMapper.selectByOrderId(orderId);
		return itemDOS.stream().map(this::itemDO2ItemBO).collect(Collectors.toList());
	}

	private ItemBO itemDO2ItemBO(ItemDO itemDO) {
		ItemBO itemBO = new ItemBO();
		itemBO.setId(itemDO.getId());
		itemBO.setOrderId(itemDO.getOrderId());
		itemBO.setGoodsId(itemDO.getGoodsId());
		itemBO.setGoodsNumber(itemDO.getGoodsNumber());
		itemBO.setCreatedAt(itemDO.getCreatedAt());

		GoodsDO goodsDO = goodsMapper.selectByPrimaryKey(itemDO.getGoodsId());
		if (goodsDO != null) {
			itemBO.setGoodsName(goodsDO.getName());
		}

		return itemBO;
	}

	@UseMaster
	@Override
	public boolean deleteItem(int itemId) {
		ItemDO itemDO = itemMapper.selectByPrimaryKey(itemId);
		if (itemDO == null) {
			FtException.throwException("订单项不存在");
		}

		long orderId = itemDO.getOrderId();
		OrderDO orderDO = orderMapper.selectByPrimaryKey(orderId);
		if (orderDO == null) {
			FtException.throwException("订单不存在");
		}

		String lockKey = RedisUtil.getRedisKey(RedisKey.REDIS_ORDER_UPDATE_LOCK, orderDO.getId() + "");

		try {
			redisLock.lock(lockKey, 10_000L);

			orderDO = orderMapper.selectByPrimaryKey(orderId);
			if (!ObjectUtil.equals(orderDO.getStatus(), OrderStatusEnum.WAIT_TO_CONFIRMED.getStatus())) {
				FtException.throwException("只有待确认订单才可删除订单项");
			}

			return itemMapper.deleteByPrimaryKey(itemId) == 1;
		} finally {
			redisLock.unlock(lockKey);
		}

	}

	@UseMaster
	@Override
	public boolean updateItem(ItemUpdateAO itemUpdateAO) {
		int itemId = itemUpdateAO.getItemId();
		ItemDO itemDO = itemMapper.selectByPrimaryKey(itemId);
		if (itemDO == null) {
			FtException.throwException("订单项不存在");
		}

		long orderId = itemDO.getOrderId();
		OrderDO orderDO = orderMapper.selectByPrimaryKey(orderId);
		if (orderDO == null) {
			FtException.throwException("订单不存在");
		}

		String lockKey = RedisUtil.getRedisKey(RedisKey.REDIS_ORDER_UPDATE_LOCK, orderDO.getId() + "");

		try {
			redisLock.lock(lockKey, 10_000L);

			orderDO = orderMapper.selectByPrimaryKey(orderId);
			if (!ObjectUtil.equals(orderDO.getStatus(), OrderStatusEnum.WAIT_TO_CONFIRMED.getStatus())) {
				FtException.throwException("只有待确认订单才可修改订单项");
			}

			int goodsId = itemUpdateAO.getGoodsId();
			GoodsDO goodsDO = goodsMapper.selectByPrimaryKey(goodsId);
			if (goodsDO == null) {
				FtException.throwException("商品不存在");
			}

			ItemDO update = new ItemDO();
			update.setId(itemId);
			update.setGoodsId(goodsId);
			update.setGoodsNumber(itemUpdateAO.getGoodsNumber());

			return itemMapper.updateByPrimaryKeySelective(update) == 1;
		} finally {
			redisLock.unlock(lockKey);
		}

	}

	@Override
	public boolean orderSuccess(long orderId, int userId) {
		String lockKey = RedisUtil.getRedisKey(RedisKey.REDIS_ORDER_UPDATE_LOCK, orderId + "");

		try {
			redisLock.lock(lockKey, 10_000L);

			OrderDO orderDO = orderMapper.selectByPrimaryKey(orderId);
			if (orderDO == null) {
				FtException.throwException("订单不存在");
			}

			if (!ObjectUtil.equals(orderDO.getStatus(), OrderStatusEnum.HAS_BEEN_CONFIRMED.getStatus())) {
				FtException.throwException("非确认状态订单");
			}

			OrderDO update = new OrderDO();
			update.setId(orderId);
			update.setOperator(userId);
			update.setStatus(OrderStatusEnum.SUCCESS.getStatus());
			update.setFinalOperateTime(System.currentTimeMillis());
			return orderMapper.updateByPrimaryKeySelective(update) == 1;
		} finally {
			redisLock.unlock(lockKey);
		}
	}

	@UseMaster
	@Transactional(value = DbConstant.DB_CONSIGN + DbConstant.TRAN_SACTION_MANAGER, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public boolean confirmOrder(Long orderId, int userId) {
		String lockKey = RedisUtil.getRedisKey(RedisKey.REDIS_ORDER_UPDATE_LOCK, orderId + "");

		try {
			redisLock.lock(lockKey, 10_000L);

			OrderDO orderDO = orderMapper.selectByPrimaryKey(orderId);
			if (orderDO == null) {
				FtException.throwException("订单不存在");
			}

			if (!ObjectUtil.equals(OrderStatusEnum.WAIT_TO_CONFIRMED.getStatus(), orderDO.getStatus())) {
				FtException.throwException("非待确认工单");
			}

			OrderDO update = new OrderDO();
			update.setId(orderId);
			update.setOperator(userId);
			update.setStatus(OrderStatusEnum.HAS_BEEN_CONFIRMED.getStatus());
			update.setConfirmTime(System.currentTimeMillis());
			orderMapper.updateByPrimaryKeySelective(update);

			this.outStock(orderId, userId);

		} finally {
			redisLock.unlock(lockKey);
		}

		return Boolean.TRUE;
	}

	private void outStock(Long orderId, int userId) {
		List<ItemDO> itemDOS = itemMapper.selectByOrderId(orderId);
		if (ObjectUtil.isEmpty(itemDOS)) {
			LogBO logBO = LogUtil.log("订单项不存在, 出库失败",
					LogAO.build("orderId", orderId + ""));
			log.info(logBO.getLogPattern(), logBO.getParams());
			return;
		}

		itemDOS.forEach(itemDO -> {
			int goodsId = itemDO.getGoodsId();
			int goodsNumber = itemDO.getGoodsNumber();

			String lockKey = RedisUtil.getRedisKey(RedisKey.REDIS_GOODS_UPDATE_LOCK, goodsId + "");

			try {
				redisLock.lock(lockKey, 10_000L);

				GoodsDO goodsDO = goodsMapper.selectByPrimaryKey(goodsId);
				if (goodsDO == null) {
					FtException.throwException("商品不存在");
				}

				if (goodsNumber > goodsDO.getNumber()) {
					FtException.throwException("商品库存不足",
							LogAO.build("商品名称", goodsDO.getName()));
				}

				StockLogDO stockLogDO = new StockLogDO();
				stockLogDO.setOperator(userId);
				stockLogDO.setType(StockLogTypeEnum.OUT.getType());
				stockLogDO.setTypeDetail(StockLogTypeDetailEnum.OUT_ORDER.getTypeDetail());
				stockLogDO.setGoodsId(goodsId);
				stockLogDO.setBeforeStockNumber(goodsService.getStock(goodsId));
				stockLogDO.setGoodsNumber(goodsNumber);

				// 出库
				goodsMapper.updateNumber(goodsId, -goodsNumber);

				stockLogDO.setAfterStockNumber(goodsService.getStock(goodsId));
				stockLogDO.setOrderId(orderId);
				stockLogDO.setItemId(itemDO.getId());

				stockLogMapper.insertSelective(stockLogDO);
			} finally {
				redisLock.unlock(lockKey);
			}
		});
	}

	@UseMaster
	@Transactional(value = DbConstant.DB_CONSIGN + DbConstant.TRAN_SACTION_MANAGER, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public boolean failOrder(Long orderId, int userId) {
		String lockKey = RedisUtil.getRedisKey(RedisKey.REDIS_ORDER_UPDATE_LOCK, orderId + "");

		try {
			redisLock.lock(lockKey, 10_000L);

			OrderDO orderDO = orderMapper.selectByPrimaryKey(orderId);
			if (orderDO == null) {
				FtException.throwException("订单不存在");
			}

			if (!ObjectUtil.equals(OrderStatusEnum.HAS_BEEN_CONFIRMED.getStatus(), orderDO.getStatus())) {
				FtException.throwException("非已确认工单");
			}

			OrderDO update = new OrderDO();
			update.setId(orderId);
			update.setOperator(userId);
			update.setStatus(OrderStatusEnum.FAIL.getStatus());
			update.setFinalOperateTime(System.currentTimeMillis());

			orderMapper.updateByPrimaryKeySelective(update);

			this.backStock(orderId, userId);
		} finally {
			redisLock.unlock(lockKey);
		}

		return Boolean.TRUE;
	}

	private void backStock(Long orderId, int userId) {
	}

	/**
	 * 订单失败
	 *
	 * @param orderId 订单id
	 * @param userId  用户id
	 * @return true:确认订单失败成功
	 */
	@UseMaster
	public boolean fail(Long orderId, int userId) {

		OrderDO orderDO = orderMapper.selectByPrimaryKey(orderId);
		OrderVO order = new OrderVO();
		if (order == null) {
			FtException.throwException("确认订单fail失败, 订单不存在");
		}

		if (order.getStatus() != OrderStatusEnum.HAS_BEEN_CONFIRMED.getStatus().intValue()) {
			FtException.throwException("确认订单fail失败, 订单已经不是已确认状态了");
		}

		String lockKey = StringUtil.append(StringUtil.REDIS_SPLIT, "fail", "orderId", orderId + "");
		Boolean flag = valueOperationsCache.setIfAbsent(lockKey, orderId + "", 5_000L, TimeUnit.MILLISECONDS);
		if (!flag) {
			FtException.throwException("确认订单fail失败, 请不要重复确认");
		}

		OrderDO update = new OrderDO();
		update.setId(order.getId());
		update.setOperator(userId);
		update.setStatus(OrderStatusEnum.FAIL.getStatus());
		orderMapper.updateByPrimaryKeySelective(update);

		List<ItemDO> itemDOS = itemMapper.selectByOrderId(orderId);
		// 将商品退回仓库
		List<ItemVO> items = new ArrayList<>();
		items.forEach(item -> {
			goodsMapper.updateNumber(item.getGoodsId(), item.getGoodsNumber());

			StockLogDO stockLogDO = new StockLogDO();
			stockLogDO.setOperator(userId);
			stockLogDO.setType(StockLogTypeEnum.IN.getType());
			stockLogDO.setTypeDetail(StockLogTypeDetailEnum.IN_ORDER.getTypeDetail());
			stockLogDO.setGoodsId(item.getGoodsId());
			stockLogDO.setGoodsNumber(item.getGoodsNumber());
			stockLogDO.setOrderId(orderId);
			stockLogDO.setRemark("");
			stockLogMapper.insertSelective(stockLogDO);
		});

		return true;
	}
}
