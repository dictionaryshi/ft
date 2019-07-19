package com.ft.br.service;

import com.ft.br.constant.OrderStatusEnum;
import com.ft.br.constant.StockLogTypeDetailEnum;
import com.ft.br.constant.StockLogTypeEnum;
import com.ft.br.dao.*;
import com.ft.br.model.dto.OrderDTO;
import com.ft.br.model.mdo.StockLogDO;
import com.ft.br.model.vo.ItemVO;
import com.ft.br.model.vo.OrderVO;
import com.ft.dao.stock.model.GoodsDO;
import com.ft.dao.stock.model.ItemDO;
import com.ft.dao.stock.model.OrderDO;
import com.ft.db.annotation.UseMaster;
import com.ft.db.constant.DbConstant;
import com.ft.db.model.PageParam;
import com.ft.db.model.PageResult;
import com.ft.redis.base.ValueOperationsCache;
import com.ft.util.StringUtil;
import com.ft.util.exception.FtException;
import com.ft.web.model.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 订单业务类
 *
 * @author shichunyang
 */
@Service
@Slf4j
public class OrderService {

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

	public PageResult<OrderVO> list(OrderDTO orderDTO, PageParam pageParam) {
		PageResult<OrderVO> pageResult = new PageResult<>();
		pageResult.setPage(pageParam.getPage());
		pageResult.setLimit(pageParam.getLimit());

		int count = orderMapper.countPagination(orderDTO);
		if (count == 0) {
			pageResult.setTotal(0);
			pageResult.setList(new ArrayList<>());
			return pageResult;
		}

		orderDTO.setStartRow(pageParam.getStartRowNumber());
		orderDTO.setPageSize(pageParam.getLimit());

		List<OrderVO> orders = orderMapper.listPagination(orderDTO);
		orders.forEach(this::format);

		pageResult.setTotal(count);
		pageResult.setList(orders);

		return pageResult;
	}

	/**
	 * 格式化订单信息
	 *
	 * @param order 订单信息
	 */
	private void format(OrderVO order) {
		// 操作人
		long operator = order.getOperator();
		UserDO userDO = userMapper.getUserById(operator);
		if (userDO != null) {
			order.setOperatorName(userDO.getUsername());
		}

		String orderStatus = null;
		OrderStatusEnum orderStatusEnum = OrderStatusEnum.getByStatus(order.getStatus());
		if (orderStatusEnum != null) {
			orderStatus = orderStatusEnum.getMessage();
		}
		// 订单中文状态
		order.setStatusCH(orderStatus);
	}

	/**
	 * 获取订单信息
	 *
	 * @param id 订单id
	 * @return 订单信息
	 */
	public OrderVO get(Long id) {
		OrderVO order = orderMapper.getOrderById(id);
		if (order == null) {
			return new OrderVO();
		}

		this.format(order);
		return order;
	}

	/**
	 * 查询订单项
	 *
	 * @param id 订单id
	 * @return 订单项
	 */
	public List<ItemVO> listItems(Long id) {
		List<ItemVO> items = itemMapper.selectByOrderId(id);
		if (items == null || items.isEmpty()) {
			return new ArrayList<>();
		}

		items.forEach(this::format);

		return items;
	}

	public void format(ItemVO item) {
		GoodsDO goodsDO = goodsMapper.getGoodsById(item.getGoodsId().intValue());
		if (goodsDO == null) {
			return;
		}

		item.setGoodsName(goodsDO.getName());
	}

	/**
	 * 添加订单信息
	 *
	 * @param orderDTO 订单信息
	 * @return true:添加成功
	 */
	@Transactional(value = DbConstant.DB_CONSIGN + DbConstant.TRAN_SACTION_MANAGER, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public boolean add(OrderDTO orderDTO) {

		OrderDO orderDO = new OrderDO();
		Long orderId = null;
		orderDO.setId(orderId);
		orderDO.setOperator(orderDTO.getOperator());
		orderDO.setStatus(OrderStatusEnum.WAIT_TO_CONFIRMED.getStatus());
		orderDO.setUsername(orderDTO.getUsername());
		orderDO.setPhone(orderDTO.getPhone());
		orderDO.setAddress(orderDTO.getAddress());
		orderDO.setTotalAmount(orderDTO.getTotalAmount());
		orderDO.setRemark(orderDTO.getRemark());
		int flag = orderMapper.insert(orderDO);

		return flag == 1;
	}

	public void checkItem(ItemDO item) {

		if (item.getGoodsNumber() != null) {
			int goodsNumber = item.getGoodsNumber();
			if (goodsNumber < 1) {
				FtException.throwException("校验订单项失败, 商品数量不能小于1");
			}
		}

		if (item.getGoodsId() != null) {
			int goodsId = item.getGoodsId().intValue();
			GoodsDO goodsDO = goodsMapper.getGoodsById(goodsId);
			if (goodsDO == null) {
				FtException.throwException("校验订单项失败, 商品不存在");
			}
		}

		Long orderId = item.getOrderId();
		OrderVO order = orderMapper.getOrderById(orderId);
		if (order == null) {
			FtException.throwException("校验订单项失败, 订单不存在");
		}

		if (order.getStatus() != OrderStatusEnum.WAIT_TO_CONFIRMED.getStatus().intValue()) {
			FtException.throwException("校验订单项失败, 订单已经不是待确认状态了");
		}

		if (item.getId() != null) {
			ItemDO itemDO = itemMapper.selectById(item.getId());
			if (itemDO == null) {
				FtException.throwException("校验订单项失败, 订单项不存在");
			}

			if (!itemDO.getOrderId().equals(orderId)) {
				FtException.throwException("校验订单项失败, 订单项与订单不匹配");
			}
		}
	}

	/**
	 * 修改订单信息
	 *
	 * @param orderDO 订单信息
	 * @return true:修改成功
	 */
	@UseMaster
	public boolean update(OrderDO orderDO) {
		return orderMapper.update(orderDO) == 1;
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
		return itemMapper.delete(id) == 1;
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

		return itemMapper.update(itemDO) == 1;
	}

	/**
	 * 添加订单项信息
	 *
	 * @param orderId     订单id
	 * @param goodsId     商品id
	 * @param goodsNumber 商品数量
	 * @return true:添加成功
	 */
	@UseMaster
	public boolean addItem(Long orderId, int goodsId, int goodsNumber) {
		ItemDO item = new ItemDO();
		item.setOrderId(orderId);
		item.setGoodsId(goodsId);
		item.setGoodsNumber(goodsNumber);

		// 核查订单项
		this.checkItem(item);

		return itemMapper.insert(item) == 1;
	}

	/**
	 * 确认订单
	 *
	 * @param orderId 订单id
	 * @return 订单确认结果
	 */
	@Transactional(value = DbConstant.DB_CONSIGN + DbConstant.TRAN_SACTION_MANAGER, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public boolean confirm(Long orderId, int userId) {
		OrderVO order = orderMapper.getOrderById(orderId);
		if (order == null) {
			FtException.throwException("订单确认失败, 订单不存在");
		}

		if (order.getStatus() != OrderStatusEnum.WAIT_TO_CONFIRMED.getStatus().intValue()) {
			FtException.throwException("订单确认失败, 订单已经不是待确认状态了");
		}

		// 查询订单的所有订单项
		List<ItemVO> items = itemMapper.selectByOrderId(orderId);
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
		orderMapper.update(update);

		// 对订单的商品进行出库处理
		items.forEach(item -> {
			goodsMapper.updateNumber(item.getGoodsId(), item.getGoodsNumber() * -1);

			StockLogDO stockLogDO = new StockLogDO();
			stockLogDO.setOperator((long) userId);
			stockLogDO.setType(StockLogTypeEnum.OUT.getType());
			stockLogDO.setTypeDetail(StockLogTypeDetailEnum.OUT_ORDER.getTypeDetail());
			stockLogDO.setGoodsId(item.getGoodsId().longValue());
			stockLogDO.setGoodsNumber(item.getGoodsNumber());
			stockLogDO.setOrderId(orderId);
			stockLogDO.setRemark("");
			stockLogMapper.insert(stockLogDO);
		});

		return true;
	}

	private void checkConfirmItem(ItemVO item) {
		int goodsId = item.getGoodsId().intValue();
		int goodsNumber = item.getGoodsNumber();
		GoodsDO goodsDO = goodsMapper.getGoodsById(goodsId);
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
		OrderVO order = orderMapper.getOrderById(orderId);
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
		orderMapper.update(update);

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

		OrderVO order = orderMapper.getOrderById(orderId);
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
		orderMapper.update(update);

		// 将商品退回仓库
		List<ItemVO> items = itemMapper.selectByOrderId(orderId);
		items.forEach(item -> {
			goodsMapper.updateNumber(item.getGoodsId(), item.getGoodsNumber());

			StockLogDO stockLogDO = new StockLogDO();
			stockLogDO.setOperator((long) userId);
			stockLogDO.setType(StockLogTypeEnum.IN.getType());
			stockLogDO.setTypeDetail(StockLogTypeDetailEnum.IN_ORDER.getTypeDetail());
			stockLogDO.setGoodsId(item.getGoodsId().longValue());
			stockLogDO.setGoodsNumber(item.getGoodsNumber());
			stockLogDO.setOrderId(orderId);
			stockLogDO.setRemark("");
			stockLogMapper.insert(stockLogDO);
		});

		return true;
	}
}
