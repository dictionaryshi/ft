package com.ft.br.controller;

import com.ft.br.model.dto.OrderDTO;
import com.ft.br.service.OrderService;
import com.ft.dao.stock.model.OrderDO;
import com.ft.db.model.PageParam;
import com.ft.util.JsonUtil;
import com.ft.util.StringUtil;
import com.ft.util.model.RestResult;
import com.ft.web.annotation.LoginCheck;
import com.ft.web.constant.SwaggerConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

	@ApiOperation("分页查询订单信息")
	/**
	 * 分页查询订单信息
	 *
	 * @param id          订单id
	 * @param currentPage 当前页码
	 * @return 当前页对应的订单数据
	 */
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "订单id", dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
			@ApiImplicitParam(name = "status", value = "订单状态", dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
			@ApiImplicitParam(name = "start", value = "开始时间", dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_QUERY),
			@ApiImplicitParam(name = "end", value = "结束时间", dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_QUERY),
			@ApiImplicitParam(name = "current_page", value = "查询页码", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
	})
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@LoginCheck
	public String list(
			@RequestParam(value = "id", required = false, defaultValue = "0") Long id,
			@RequestParam(value = "status", required = false, defaultValue = "99") Integer status,
			@RequestParam(value = "start", required = false) String start,
			@RequestParam(value = "end", required = false) String end,
			@RequestParam(value = "current_page") int currentPage
	) {
		PageParam pageParam = new PageParam(currentPage, 10);

		Long defaultId = 0L;
		OrderDTO orderDTO = new OrderDTO();
		if (!defaultId.equals(id)) {
			orderDTO.setId(id);
		}

		int defaultStatus = 99;
		if (status != defaultStatus) {
			orderDTO.setStatus(status);
		}

		if (!StringUtil.isNull(start)) {
			orderDTO.setStartDate(start);
		}
		if (!StringUtil.isNull(end)) {
			orderDTO.setEndDate(end);
		}

		return JsonUtil.object2Json(RestResult.success(orderService.list(orderDTO, pageParam)));
	}

	@ApiOperation("获取某个订单信息")
	/**
	 * 获取某个订单信息
	 *
	 * @param id 订单主键
	 * @return 订单信息
	 */
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
	})
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	@LoginCheck
	public String get(@RequestParam(value = "id") Long id) {
		return JsonUtil.object2Json(RestResult.success(orderService.get(id)));
	}

	@ApiOperation("查询订单项")
	/**
	 * 查询订单项
	 *
	 * @param id 订单id
	 * @return 订单项
	 */
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
	})
	@RequestMapping(value = "/list-items", method = RequestMethod.POST)
	@LoginCheck
	public String listItems(@RequestParam(value = "id") Long id) {
		return JsonUtil.object2Json(RestResult.success(orderService.listItems(id)));
	}

	@ApiOperation("添加订单信息")
	/**
	 * 添加订单信息
	 *
	 * @param totalAmount 订单总金额
	 * @param username    用户姓名
	 * @param phone       手机号
	 * @param address     地址
	 * @param remark      备注信息
	 * @return 添加结果
	 */
	@ApiImplicitParams({
			@ApiImplicitParam(name = "total_amount", value = "订单总金额", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
			@ApiImplicitParam(name = "username", value = "客户姓名", dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_QUERY),
			@ApiImplicitParam(name = "phone", value = "客户电话", dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_QUERY),
			@ApiImplicitParam(name = "address", value = "客户地址", dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_QUERY),
			@ApiImplicitParam(name = "remark", value = "订单备注", dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_QUERY),
	})
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@LoginCheck
	public String add(
			HttpServletRequest request,
			@RequestParam(value = "total_amount") double totalAmount,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "remark", required = false) String remark
	) {
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setTotalAmount(totalAmount);
		orderDTO.setUsername(username);
		orderDTO.setPhone(phone);
		orderDTO.setAddress(address);
		orderDTO.setRemark(remark);
		orderDTO.setOperator(0);

		boolean flag = orderService.add(orderDTO);
		return JsonUtil.object2Json(RestResult.success(flag));
	}

	@ApiOperation("修改订单")
	/**
	 * 修改订单
	 *
	 * @param request     请求对象
	 * @param id          订单id
	 * @param totalAmount 总金额
	 * @param username    用户名
	 * @param phone       电话
	 * @param address     地址
	 * @param remark      备注
	 * @return 修改结果
	 */
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
			@ApiImplicitParam(name = "total_amount", value = "订单总金额", dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
			@ApiImplicitParam(name = "username", value = "客户姓名", dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_QUERY),
			@ApiImplicitParam(name = "phone", value = "客户电话", dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_QUERY),
			@ApiImplicitParam(name = "address", value = "客户地址", dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_QUERY),
			@ApiImplicitParam(name = "remark", value = "订单备注", dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_QUERY),
	})
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@LoginCheck
	public String update(
			HttpServletRequest request,
			@RequestParam(value = "id") Long id,
			@RequestParam(value = "total_amount", required = false, defaultValue = "0.0") double totalAmount,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "remark", required = false) String remark
	) {
		OrderDO orderDO = new OrderDO();
		orderDO.setOperator(0);
		orderDO.setUsername(username);
		orderDO.setPhone(phone);
		orderDO.setAddress(address);

		double defaultAmount = 0.0;
		if (totalAmount != defaultAmount && totalAmount > defaultAmount) {
			orderDO.setTotalAmount(totalAmount);
		}
		orderDO.setRemark(remark);
		orderDO.setId(id);

		boolean flag = orderService.update(orderDO);
		return JsonUtil.object2Json(RestResult.success(flag));
	}

	@ApiOperation("添加订单项")
	/**
	 * 添加订单项
	 *
	 * @param orderId     订单id
	 * @param goodsId     商品id
	 * @param goodsNumber 商品数量
	 * @return 添加订单项结果
	 */
	@ApiImplicitParams({
			@ApiImplicitParam(name = "order_id", value = "订单id", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
			@ApiImplicitParam(name = "goods_id", value = "商品id", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
			@ApiImplicitParam(name = "goods_number", value = "商品数量", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
	})
	@LoginCheck
	@RequestMapping(value = "/add-item", method = RequestMethod.POST)
	public String addItem(
			@RequestParam("order_id") Long orderId,
			@RequestParam("goods_id") int goodsId,
			@RequestParam("goods_number") int goodsNumber
	) {
		boolean flag = orderService.addItem(orderId, goodsId, goodsNumber);
		return JsonUtil.object2Json(RestResult.success(flag));
	}

	@ApiOperation("删除订单项")
	/**
	 * 删除订单项目
	 *
	 * @param id 订单项id
	 * @return 删除结果
	 */
	@ApiImplicitParams({
			@ApiImplicitParam(name = "order_id", value = "订单id", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
			@ApiImplicitParam(name = "id", value = "订单项id", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
	})
	@RequestMapping(value = "/delete-item", method = RequestMethod.POST)
	@LoginCheck
	public String deleteItem(
			@RequestParam("order_id") Long orderId,
			@RequestParam("id") int id
	) {
		boolean flag = orderService.deleteItem(id, orderId);
		return JsonUtil.object2Json(RestResult.success(flag));
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
		boolean flag = orderService.updateItem(id, goodsNumber, orderId);
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
		boolean flag = orderService.confirm(orderId, userId);
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
		boolean flag = orderService.success(orderId, userId);
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
		boolean flag = orderService.fail(orderId, userId);
		return JsonUtil.object2Json(RestResult.success(flag));
	}
}
