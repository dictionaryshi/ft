package com.ft.br.controller;

import com.ft.br.model.ao.StockLogListAO;
import com.ft.br.model.bo.StockLogBO;
import com.ft.br.service.StockLogService;
import com.ft.br.service.impl.StockLogServiceImpl;
import com.ft.dao.stock.model.StockLogDO;
import com.ft.db.annotation.PageParamCheck;
import com.ft.db.model.PageResult;
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
import javax.validation.Valid;

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

	@Autowired
	private StockLogServiceImpl stockLogServiceImpl;

	@ApiOperation("分页查询库存操作记录")
	@LoginCheck
	@PageParamCheck
	@GetMapping("/list")
	public RestResult<PageResult<StockLogBO>> list(
			@Valid StockLogListAO stockLogListAO
	) {
		PageResult<StockLogBO> pageResult = stockLogService.listByPage(stockLogListAO);
		return RestResult.success(pageResult);
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
			@RequestParam("goods_id") int goodsId,
			@RequestParam(value = "order_id", defaultValue = "0") Long orderId,
			@RequestParam("goods_number") int goodsNumber,
			@RequestParam(value = "remark", required = false, defaultValue = "") String remark
	) {
		StockLogDO stockLogDO = new StockLogDO();
		stockLogDO.setOperator(0);
		stockLogDO.setType(type);
		stockLogDO.setGoodsId(goodsId);
		stockLogDO.setGoodsNumber(goodsNumber);
		stockLogDO.setRemark(remark);
		stockLogDO.setOrderId(orderId);

		boolean flag = stockLogServiceImpl.storage(stockLogDO);
		return JsonUtil.object2Json(RestResult.success(flag));
	}
}
