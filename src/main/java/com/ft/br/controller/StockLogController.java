package com.ft.br.controller;

import com.ft.br.model.dto.StockLogDTO;
import com.ft.br.model.mdo.StockLogDO;
import com.ft.br.service.StockLogService;
import com.ft.br.util.LoginUtil;
import com.ft.db.model.PageParam;
import com.ft.util.JsonUtil;
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
 * 出入库API
 *
 * @author shichunyang
 */
@Api(tags = "出入库API")
@RestController
@CrossOrigin
@RequestMapping(RestResult.API + "/stock")
@Slf4j
public class StockLogController {

	@Autowired
	private StockLogService stockLogService;

	@ApiOperation("库存列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "current_page", value = "查询页码", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
			@ApiImplicitParam(name = "type", value = "操作类型", dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
			@ApiImplicitParam(name = "goods_id", value = "商品id", dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
			@ApiImplicitParam(name = "order_id", value = "订单id", dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
			@ApiImplicitParam(name = "start_time", value = "开始时间", dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_QUERY),
			@ApiImplicitParam(name = "end_time", value = "结束时间", dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_QUERY),
	})
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@LoginCheck
	public String list(
			@RequestParam(value = "current_page") int currentPage,
			@RequestParam(value = "type", required = false, defaultValue = "0") Integer type,
			@RequestParam(value = "goods_id", required = false, defaultValue = "0") long goodsId,
			@RequestParam(value = "order_id", required = false) Long orderId,
			@RequestParam(value = "start_time", required = false) String startTime,
			@RequestParam(value = "end_time", required = false) String endTime
	) {
		PageParam pageParam = new PageParam(currentPage, 10);

		StockLogDTO stockLogDTO = new StockLogDTO();
		if (type != 0) {
			stockLogDTO.setType(type);
		}
		if (goodsId != 0) {
			stockLogDTO.setGoodsId(goodsId);
		}
		stockLogDTO.setOrderId(orderId);
		stockLogDTO.setStartDate(startTime);
		stockLogDTO.setEndDate(endTime);

		return JsonUtil.object2Json(RestResult.success(stockLogService.list(stockLogDTO, pageParam)));
	}

	@ApiOperation("出/入库操作")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "type", value = "操作类型", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
			@ApiImplicitParam(name = "goods_id", value = "商品id", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
			@ApiImplicitParam(name = "order_id", value = "订单id", dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
			@ApiImplicitParam(name = "goods_number", value = "商品数量", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, paramType = SwaggerConstant.PARAM_TYPE_QUERY, example = "0"),
			@ApiImplicitParam(name = "remark", value = "操作备注", dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_QUERY),
	})
	@RequestMapping(value = "/storage", method = RequestMethod.POST)
	@LoginCheck
	public String storage(
			HttpServletRequest request,
			@RequestParam("type") Integer type,
			@RequestParam("goods_id") long goodsId,
			@RequestParam(value = "order_id", defaultValue = "0") Long orderId,
			@RequestParam("goods_number") int goodsNumber,
			@RequestParam(value = "remark", required = false, defaultValue = "") String remark
	) {
		StockLogDO stockLogDO = new StockLogDO();
		stockLogDO.setOperator(LoginUtil.getLoginUser(request).getId());
		stockLogDO.setType(type);
		stockLogDO.setGoodsId(goodsId);
		stockLogDO.setGoodsNumber(goodsNumber);
		stockLogDO.setRemark(remark);
		stockLogDO.setOrderId(orderId);

		boolean flag = stockLogService.storage(stockLogDO);
		return JsonUtil.object2Json(RestResult.success(flag));
	}
}
