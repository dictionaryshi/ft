package com.ft.br.service;

import com.ft.br.constant.OrderConstant;
import com.ft.br.constant.StockConstant;
import com.ft.br.dao.*;
import com.ft.br.model.dto.OrderDTO;
import com.ft.br.model.mdo.GoodsDO;
import com.ft.br.model.mdo.ItemDO;
import com.ft.br.model.mdo.OrderDO;
import com.ft.br.model.mdo.StockLogDO;
import com.ft.br.model.vo.ItemVO;
import com.ft.br.model.vo.OrderVO;
import com.ft.db.annotation.DataSource;
import com.ft.db.constant.DbConstant;
import com.ft.db.model.PageParam;
import com.ft.db.model.PageResult;
import com.ft.redis.base.ValueOperationsCache;
import com.ft.redis.lock.RedisLock;
import com.ft.redis.util.OrderNumberUtil;
import com.ft.util.StringUtil;
import com.ft.util.exception.FtException;
import com.ft.util.model.RestResult;
import com.ft.web.constant.HystrixConstant;
import com.ft.web.model.UserDO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
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

	@DataSource(DataSource.slave)
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

		// 订单中文状态
		order.setStatusCH(OrderConstant.STATUS_MAP.get(order.getStatus()));
	}

	/**
	 * 获取订单信息
	 *
	 * @param id 订单id
	 * @return 订单信息
	 */
	@DataSource(DataSource.slave)
	public OrderVO get(String id) {
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
	@DataSource(DataSource.slave)
	public List<ItemVO> listItems(String id) {
		List<ItemVO> items = itemMapper.selectByOrderId(id);
		if (items == null || items.isEmpty()) {
			return new ArrayList<>();
		}

		items.forEach(this::format);

		return items;
	}

	public void format(ItemVO item) {
		GoodsDO goodsDO = goodsMapper.getGoodsById(item.getGoodsId());
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
	@DataSource
	@Transactional(value = DbConstant.DB_CONSIGN + DbConstant.TRAN_SACTION_MANAGER, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public boolean add(OrderDTO orderDTO) {

		OrderDO orderDO = new OrderDO();
		String orderId = OrderNumberUtil.getRandomNumber(new RedisLock(valueOperationsCache));
		orderDO.setId(orderId);
		orderDO.setOperator(orderDTO.getOperator());
		orderDO.setStatus(OrderConstant.STATUS_READY);
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
				throw new FtException(RestResult.ERROR_CODE, "校验订单项失败, 商品数量不能小于1");
			}
		}

		if (item.getGoodsId() != null) {
			long goodsId = item.getGoodsId();
			GoodsDO goodsDO = goodsMapper.getGoodsById(goodsId);
			if (goodsDO == null) {
				throw new FtException(RestResult.ERROR_CODE, "校验订单项失败, 商品不存在");
			}
		}

		String orderId = item.getOrderId();
		OrderVO order = orderMapper.getOrderById(orderId);
		if (order == null) {
			throw new FtException(RestResult.ERROR_CODE, "校验订单项失败, 订单不存在");
		}

		if (order.getStatus() != OrderConstant.STATUS_READY) {
			throw new FtException(RestResult.ERROR_CODE, "校验订单项失败, 订单已经不是待确认状态了");
		}

		if (item.getId() != null) {
			ItemDO itemDO = itemMapper.selectById(item.getId());
			if (itemDO == null) {
				throw new FtException(RestResult.ERROR_CODE, "校验订单项失败, 订单项不存在");
			}

			if (!itemDO.getOrderId().equals(orderId)) {
				throw new FtException(RestResult.ERROR_CODE, "校验订单项失败, 订单项与订单不匹配");
			}
		}
	}

	/**
	 * 修改订单信息
	 *
	 * @param orderDO 订单信息
	 * @return true:修改成功
	 */
	@DataSource
	public boolean update(OrderDO orderDO) {
		return orderMapper.update(orderDO) == 1;
	}

	/**
	 * 删除订单项目
	 *
	 * @param id 订单项id
	 * @return true:删除成功
	 */
	@DataSource
	public boolean deleteItem(long id, String orderId) {
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
	@DataSource
	public boolean updateItem(long id, int goodsNumber, String orderId) {
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
	@DataSource
	public boolean addItem(String orderId, long goodsId, int goodsNumber) {
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
	@DataSource
	@Transactional(value = DbConstant.DB_CONSIGN + DbConstant.TRAN_SACTION_MANAGER, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public boolean confirm(String orderId, long userId) {
		OrderVO order = orderMapper.getOrderById(orderId);
		if (order == null) {
			throw new FtException(RestResult.ERROR_CODE, "订单确认失败, 订单不存在");
		}

		if (order.getStatus() != OrderConstant.STATUS_READY) {
			throw new FtException(RestResult.ERROR_CODE, "订单确认失败, 订单已经不是待确认状态了");
		}

		// 查询订单的所有订单项
		List<ItemVO> items = itemMapper.selectByOrderId(orderId);
		if (items.isEmpty()) {
			throw new FtException(RestResult.ERROR_CODE, "订单确认失败, 还没有添加订单详情");
		}

		String lockKey = StringUtil.append(StringUtil.REDIS_SPLIT, "confirm", "orderId", orderId);
		Boolean flag = valueOperationsCache.setNX(lockKey, orderId, 5_000L);
		if (!flag) {
			throw new FtException(RestResult.ERROR_CODE, "订单确认失败, 请不要重复确认");
		}

		items.forEach(this::checkConfirmItem);

		OrderDO update = new OrderDO();
		update.setId(order.getId());
		update.setOperator(userId);
		update.setStatus(OrderConstant.STATUS_CONFIRM);
		orderMapper.update(update);

		// 对订单的商品进行出库处理
		items.forEach(item -> {
			goodsMapper.updateNumber(item.getGoodsId(), item.getGoodsNumber() * -1);

			StockLogDO stockLogDO = new StockLogDO();
			stockLogDO.setOperator(userId);
			stockLogDO.setType(StockConstant.TYPE_OUT);
			stockLogDO.setTypeDetail(StockConstant.TYPE_DETAIL_OUT_ORDER);
			stockLogDO.setGoodsId(item.getGoodsId());
			stockLogDO.setGoodsNumber(item.getGoodsNumber());
			stockLogDO.setOrderId(orderId);
			stockLogDO.setRemark("");
			stockLogMapper.insert(stockLogDO);
		});

		return true;
	}

	private void checkConfirmItem(ItemVO item) {
		long goodsId = item.getGoodsId();
		int goodsNumber = item.getGoodsNumber();
		GoodsDO goodsDO = goodsMapper.getGoodsById(goodsId);
		if (goodsDO == null) {
			throw new FtException(RestResult.ERROR_CODE, "订单确认失败, 订单项中商品不存在");
		}

		if (goodsNumber > goodsDO.getNumber()) {
			throw new FtException(RestResult.ERROR_CODE, "订单确认失败, 商品库存数量不足");
		}
	}

	/**
	 * 订单成功
	 *
	 * @param orderId 订单id
	 * @param userId  用户id
	 * @return true:订单成功
	 */
	public boolean success(String orderId, long userId) {
		OrderVO order = orderMapper.getOrderById(orderId);
		if (order == null) {
			throw new FtException(RestResult.ERROR_CODE, "确认订单success失败, 订单不存在");
		}

		if (order.getStatus() != OrderConstant.STATUS_CONFIRM) {
			throw new FtException(RestResult.ERROR_CODE, "确认订单success失败, 订单已经不是已确认状态了");
		}

		OrderDO update = new OrderDO();
		update.setId(order.getId());
		update.setOperator(userId);
		update.setStatus(OrderConstant.STATUS_SUCCESS);
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
	public boolean fail(String orderId, long userId) {

		OrderVO order = orderMapper.getOrderById(orderId);
		if (order == null) {
			throw new FtException(RestResult.ERROR_CODE, "确认订单fail失败, 订单不存在");
		}

		if (order.getStatus() != OrderConstant.STATUS_CONFIRM) {
			throw new FtException(RestResult.ERROR_CODE, "确认订单fail失败, 订单已经不是已确认状态了");
		}

		String lockKey = StringUtil.append(StringUtil.REDIS_SPLIT, "fail", "orderId", orderId);
		Boolean flag = valueOperationsCache.setNX(lockKey, orderId, 5_000L);
		if (!flag) {
			throw new FtException(RestResult.ERROR_CODE, "确认订单fail失败, 请不要重复确认");
		}

		OrderDO update = new OrderDO();
		update.setId(order.getId());
		update.setOperator(userId);
		update.setStatus(OrderConstant.STATUS_FAIL);
		orderMapper.update(update);

		// 将商品退回仓库
		List<ItemVO> items = itemMapper.selectByOrderId(orderId);
		items.forEach(item -> {
			goodsMapper.updateNumber(item.getGoodsId(), item.getGoodsNumber());

			StockLogDO stockLogDO = new StockLogDO();
			stockLogDO.setOperator(userId);
			stockLogDO.setType(StockConstant.TYPE_IN);
			stockLogDO.setTypeDetail(StockConstant.TYPE_DETAIL_IN_ORDER);
			stockLogDO.setGoodsId(item.getGoodsId());
			stockLogDO.setGoodsNumber(item.getGoodsNumber());
			stockLogDO.setOrderId(orderId);
			stockLogDO.setRemark("");
			stockLogMapper.insert(stockLogDO);
		});

		return true;
	}

	@CacheResult(cacheKeyMethod = "orderCacheKey")
	@HystrixCommand(
			groupKey = "orderService",
			threadPoolKey = "orderQuery",
			threadPoolProperties = {
					@HystrixProperty(name = HystrixConstant.CORE_SIZE, value = "10"),
					@HystrixProperty(name = HystrixConstant.KEEP_ALIVE_TIME_MINUTES, value = "5"),
					@HystrixProperty(name = HystrixConstant.QUEUE_SIZE_REJECTION_THRESHOLD, value = "16"),
					@HystrixProperty(name = HystrixConstant.MAX_QUEUE_SIZE, value = "20"),
			},
			commandProperties = {
					@HystrixProperty(name = HystrixConstant.EXECUTION_ISOLATION_STRATEGY, value = HystrixConstant.EXECUTION_ISOLATION_STRATEGY_THREAD),
					@HystrixProperty(name = HystrixConstant.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLI_SECONDS, value = "2000"),
					@HystrixProperty(name = HystrixConstant.EXECUTION_ISOLATION_SEMAPHORE_MAXCONCURRENTREQUESTS, value = "10"),
					@HystrixProperty(name = HystrixConstant.CIRCUITBREAKER_ENABLED, value = "true")
			},
			fallbackMethod = "hystrixFallBack"
	)
	public Future<OrderDO> hystrix(String id) {
		return new AsyncResult<OrderDO>() {
			@Override
			public OrderDO invoke() {
				try {
					System.out.println("hystrix orderId==>" + id + " start");
					long start = System.currentTimeMillis();
					int target = 1250;
					for (int i = 0; i < target; i++) {
						for (int j = 0; j < target; j++) {
							System.out.print("");
						}
					}
					long end = System.currentTimeMillis();
					OrderDO orderDO = new OrderDO();
					orderDO.setId(id);
					orderDO.setRemark("订单成功");
					System.out.println("hystrix orderId==>" + id + " end, cost==>" + (end - start));
					return orderDO;
				} catch (Throwable e) {
					log.warn("hystrix orderId==>{}, exception==>{}", id, FtException.getExceptionStack(e));
					throw e;
				}
			}
		};
	}

	public OrderDO hystrixFallBack(String id) {
		OrderDO orderDO = new OrderDO();
		orderDO.setId(id);
		orderDO.setRemark("hystrix熔断");
		return orderDO;
	}

	public String orderCacheKey(String id) {
		return "order_cache_" + id;
	}
}
