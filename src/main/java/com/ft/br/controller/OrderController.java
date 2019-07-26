package com.ft.br.controller;

import com.ft.br.constant.RedisKey;
import com.ft.br.model.ao.item.ItemAddAO;
import com.ft.br.model.ao.item.ItemDeleteAO;
import com.ft.br.model.ao.item.ItemListAO;
import com.ft.br.model.ao.order.OrderAddUpdateAO;
import com.ft.br.model.ao.order.OrderGetAO;
import com.ft.br.model.ao.order.OrderListAO;
import com.ft.br.model.bo.ItemBO;
import com.ft.br.model.bo.OrderBO;
import com.ft.br.service.IdService;
import com.ft.br.service.OrderService;
import com.ft.br.service.impl.OrderServiceImpl;
import com.ft.db.annotation.PageParamCheck;
import com.ft.db.model.PageResult;
import com.ft.redis.lock.RedisLock;
import com.ft.redis.util.RedisUtil;
import com.ft.util.JsonUtil;
import com.ft.util.exception.FtException;
import com.ft.util.model.RestResult;
import com.ft.web.annotation.LoginCheck;
import com.ft.web.constant.SwaggerConstant;
import com.ft.web.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 订单API
 *
 * @author shichunyang
 */
@Api(tags = "订单API")
@RestController
@CrossOrigin
@RequestMapping(RestResult.API + "/order")
@Slf4j
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderServiceImpl orderServiceImpl;

	@Autowired
	private IdService idService;

	@Autowired
	private RedisLock redisLock;

	@ApiOperation("获取订单id")
	@LoginCheck
	@GetMapping("/get-order-id")
	public RestResult<Long> getOrderId() {
		Long orderId = idService.getId();
		if (orderId == null || orderId <= 0) {
			FtException.throwException("订单id获取失败");
		}

		return RestResult.success(orderId);
	}

	@ApiOperation("创建订单")
	@LoginCheck
	@PostMapping("/create")
	public RestResult<Boolean> create(
			@RequestBody @Valid OrderAddUpdateAO orderAddAO
	) {
		int operator = WebUtil.getCurrentUser().getId();
		orderAddAO.setOperator(operator);

		String lockKey = RedisUtil.getRedisKey(RedisKey.REDIS_ORDER_UPDATE_LOCK, orderAddAO.getId() + "");

		try {
			redisLock.lock(lockKey, 10_000L);

			boolean result = orderService.createOrder(orderAddAO);

			return RestResult.success(result);
		} finally {
			redisLock.unlock(lockKey);
		}
	}

	@ApiOperation("修改订单信息")
	@LoginCheck
	@PostMapping("/update")
	public RestResult<Boolean> update(
			@RequestBody @Valid OrderAddUpdateAO orderAddUpdateAO
	) {
		int operator = WebUtil.getCurrentUser().getId();
		orderAddUpdateAO.setOperator(operator);

		boolean result = orderService.updateOrder(orderAddUpdateAO);

		return RestResult.success(result);
	}

	@ApiOperation("根据id查询订单信息")
	@LoginCheck
	@GetMapping("/get")
	public RestResult<OrderBO> get(
			@Valid OrderGetAO orderGetAO
	) {
		return RestResult.success(orderService.getOrderById(orderGetAO));
	}

	@ApiOperation("分页查询订单信息")
	@LoginCheck
	@PageParamCheck
	@GetMapping("/list")
	public RestResult<PageResult<OrderBO>> list(
			@Valid OrderListAO orderListAO
	) {
		PageResult<OrderBO> pageResult = orderService.listByPage(orderListAO);
		return RestResult.success(pageResult);
	}

	@ApiOperation("添加订单项")
	@LoginCheck
	@PostMapping("/add-item")
	public RestResult<Boolean> addItem(
			@RequestBody @Valid ItemAddAO itemAddAO
	) {
		boolean result = orderService.addItem(itemAddAO);
		return RestResult.success(result);
	}

	@ApiOperation("查询订单项")
	@LoginCheck
	@GetMapping("/list-items")
	public RestResult<List<ItemBO>> listItems(
			@Valid ItemListAO itemListAO
	) {
		List<ItemBO> itemBOS = orderService.listItems(itemListAO.getOrderId());
		return RestResult.success(itemBOS);
	}

	@ApiOperation("删除订单项")
	@LoginCheck
	@PostMapping("/delete-item")
	public RestResult<Boolean> deleteItem(
			@RequestBody @Valid ItemDeleteAO itemDeleteAO
	) {
		boolean result = orderService.deleteItem(itemDeleteAO.getItemId());
		return RestResult.success(result);
	}

	@ApiOperation("修改订单项")
	/**
	 * 修改订单项
	 *
	 * @param goodsNumber 商品数目
	 * @param id          订单项id
	 * @return 修改订单项结果
	 */
	@ApiImplicitParams({
			@ApiImplicitParam(name = "order_id", value = "订单id", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
			@ApiImplicitParam(name = "goods_number", value = "商品数量", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
			@ApiImplicitParam(name = "id", value = "订单项id", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
	})
	@RequestMapping(value = "/update-item", method = RequestMethod.POST)
	@LoginCheck
	public String updateItem(
			@RequestParam("order_id") Long orderId,
			@RequestParam(value = "goods_number") int goodsNumber,
			@RequestParam(value = "id") int id
	) {
		boolean flag = orderServiceImpl.updateItem(id, goodsNumber, orderId);
		return JsonUtil.object2Json(RestResult.success(flag));
	}

	@ApiOperation("确认订单")
	/**
	 * 确认订单
	 *
	 * @param orderId 订单id
	 * @return 确认订单结果
	 */
	@ApiImplicitParams({
			@ApiImplicitParam(name = "order_id", value = "订单id", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
	})
	@LoginCheck
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	public String confirm(
			HttpServletRequest request,
			@RequestParam("order_id") Long orderId
	) {
		int userId = 0;
		boolean flag = orderServiceImpl.confirm(orderId, userId);
		return JsonUtil.object2Json(RestResult.success(flag));
	}

	@ApiOperation("确认订单成功")
	/**
	 * 确认订单成功
	 *
	 * @return 订单确认结果
	 */
	@ApiImplicitParams({
			@ApiImplicitParam(name = "order_id", value = "订单id", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
	})
	@LoginCheck
	@RequestMapping(value = "/success", method = RequestMethod.POST)
	public String success(
			HttpServletRequest request,
			@RequestParam("order_id") Long orderId
	) {
		int userId = 0;
		boolean flag = orderServiceImpl.success(orderId, userId);
		return JsonUtil.object2Json(RestResult.success(flag));
	}

	@ApiOperation("确认订单失败")
	/**
	 * 确认订单失败
	 *
	 * @return 订单确认失败结果
	 */
	@ApiImplicitParams({
			@ApiImplicitParam(name = "order_id", value = "订单id", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
	})
	@LoginCheck
	@RequestMapping(value = "/fail", method = RequestMethod.POST)
	public String fail(
			HttpServletRequest request,
			@RequestParam("order_id") Long orderId
	) {
		int userId = 0;
		boolean flag = orderServiceImpl.fail(orderId, userId);
		return JsonUtil.object2Json(RestResult.success(flag));
	}
}
