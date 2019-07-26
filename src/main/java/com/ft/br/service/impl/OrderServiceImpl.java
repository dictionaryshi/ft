package com.ft.br.service.impl;

import com.ft.br.constant.RedisKey;
import com.ft.br.model.ao.item.ItemAddAO;
import com.ft.br.model.bo.ItemBO;
import com.ft.redis.lock.RedisLock;
import com.ft.redis.util.RedisUtil;
import com.google.common.collect.Lists;

import com.ft.br.constant.OrderStatusEnum;
import com.ft.br.constant.StockLogTypeDetailEnum;
import com.ft.br.constant.StockLogTypeEnum;
import com.ft.br.dao.*;
import com.ft.br.model.ao.order.OrderAddUpdateAO;
import com.ft.br.model.ao.order.OrderGetAO;
import com.ft.br.model.ao.order.OrderListAO;
import com.ft.br.model.bo.OrderBO;
import com.ft.br.model.vo.ItemVO;
import com.ft.br.model.vo.OrderVO;
import com.ft.br.service.OrderService;
import com.ft.dao.stock.model.*;
import com.ft.db.annotation.UseMaster;
import com.ft.db.constant.DbConstant;
import com.ft.db.model.PageResult;
import com.ft.redis.base.ValueOperationsCache;
import com.ft.util.DateUtil;
import com.ft.util.JsonUtil;
import com.ft.util.ObjectUtil;
import com.ft.util.StringUtil;
import com.ft.util.exception.FtException;
import com.ft.util.model.LogAO;
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

	public void checkItem(ItemDO item) {
		Long orderId = item.getOrderId();
		OrderVO order = new OrderVO();

		if (order.getStatus() != OrderStatusEnum.WAIT_TO_CONFIRMED.getStatus().intValue()) {
			FtException.throwException("校验订单项失败, 订单已经不是待确认状态了");
		}

		if (item.getId() != null) {
			ItemDO itemDO = itemMapper.selectByPrimaryKey(item.getId());
			if (itemDO == null) {
				FtException.throwException("校验订单项失败, 订单项不存在");
			}

			if (!itemDO.getOrderId().equals(orderId)) {
				FtException.throwException("校验订单项失败, 订单项与订单不匹配");
			}
		}
	}

	/**
	 * 删除订单项目
	 *
	 * @param id 订单项id
	 * @return true:删除成功
	 */
	@UseMaster
	public boolean deleteItem(int id, Long orderId) {
		ItemDO item = new ItemDO();
		item.setId(id);
		item.setOrderId(orderId);
		// 核查订单项
		this.checkItem(item);
		return itemMapper.deleteByPrimaryKey(id) == 1;
	}

	/**
	 * 修改订单项
	 *
	 * @param id          订单项id
	 * @param goodsNumber 商品数量
	 * @return true:修改成功
	 */
	@UseMaster
	public boolean updateItem(int id, int goodsNumber, Long orderId) {
		ItemDO itemDO = new ItemDO();
		itemDO.setId(id);
		itemDO.setGoodsNumber(goodsNumber);
		itemDO.setOrderId(orderId);

		// 核查订单项
		this.checkItem(itemDO);

		return itemMapper.updateByPrimaryKeySelective(itemDO) == 1;
	}

	/**
	 * 确认订单
	 *
	 * @param orderId 订单id
	 * @return 订单确认结果
	 */
	@Transactional(value = DbConstant.DB_CONSIGN + DbConstant.TRAN_SACTION_MANAGER, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public boolean confirm(Long orderId, int userId) {
		OrderDO orderDO = orderMapper.selectByPrimaryKey(orderId);
		OrderVO order = new OrderVO();
		if (order == null) {
			FtException.throwException("订单确认失败, 订单不存在");
		}

		if (order.getStatus() != OrderStatusEnum.WAIT_TO_CONFIRMED.getStatus().intValue()) {
			FtException.throwException("订单确认失败, 订单已经不是待确认状态了");
		}

		List<ItemDO> itemDOS = itemMapper.selectByOrderId(orderId);
		// 查询订单的所有订单项
		List<ItemVO> items = new ArrayList<>();
		if (items.isEmpty()) {
			FtException.throwException("订单确认失败, 还没有添加订单详情");
		}

		String lockKey = StringUtil.append(StringUtil.REDIS_SPLIT, "confirm", "orderId", orderId + "");
		Boolean flag = valueOperationsCache.setIfAbsent(lockKey, orderId + "", 5_000L, TimeUnit.MILLISECONDS);
		if (!flag) {
			FtException.throwException("订单确认失败, 请不要重复确认");
		}

		items.forEach(this::checkConfirmItem);

		OrderDO update = new OrderDO();
		update.setId(order.getId());
		update.setOperator(userId);
		update.setStatus(OrderStatusEnum.HAS_BEEN_CONFIRMED.getStatus());
		orderMapper.updateByPrimaryKeySelective(update);

		// 对订单的商品进行出库处理
		items.forEach(item -> {
			goodsMapper.updateNumber(item.getGoodsId(), item.getGoodsNumber() * -1);

			StockLogDO stockLogDO = new StockLogDO();
			stockLogDO.setOperator(userId);
			stockLogDO.setType(StockLogTypeEnum.OUT.getType());
			stockLogDO.setTypeDetail(StockLogTypeDetailEnum.OUT_ORDER.getTypeDetail());
			stockLogDO.setGoodsId(item.getGoodsId());
			stockLogDO.setGoodsNumber(item.getGoodsNumber());
			stockLogDO.setOrderId(orderId);
			stockLogDO.setRemark("");
			stockLogMapper.insertSelective(stockLogDO);
		});

		return true;
	}

	private void checkConfirmItem(ItemVO item) {
		int goodsId = item.getGoodsId();
		int goodsNumber = item.getGoodsNumber();
		GoodsDO goodsDO = goodsMapper.selectByPrimaryKey(goodsId);
		if (goodsDO == null) {
			FtException.throwException("订单确认失败, 订单项中商品不存在");
		}

		if (goodsNumber > goodsDO.getNumber()) {
			FtException.throwException("订单确认失败, 商品库存数量不足");
		}
	}

	/**
	 * 订单成功
	 *
	 * @param orderId 订单id
	 * @param userId  用户id
	 * @return true:订单成功
	 */
	@UseMaster
	public boolean success(Long orderId, int userId) {
		OrderDO orderDO = orderMapper.selectByPrimaryKey(orderId);
		OrderVO order = new OrderVO();
		if (order == null) {
			FtException.throwException("确认订单success失败, 订单不存在");
		}

		if (order.getStatus() != OrderStatusEnum.HAS_BEEN_CONFIRMED.getStatus().intValue()) {
			FtException.throwException("确认订单success失败, 订单已经不是已确认状态了");
		}

		OrderDO update = new OrderDO();
		update.setId(order.getId());
		update.setOperator(userId);
		update.setStatus(OrderStatusEnum.SUCCESS.getStatus());
		orderMapper.updateByPrimaryKeySelective(update);

		return true;
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
