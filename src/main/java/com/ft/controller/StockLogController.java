package com.ft.controller;

import com.ft.db.model.PageParam;
import com.ft.util.JsonUtil;
import com.ft.web.model.RestResult;
import com.ft.annotation.LoginCheck;
import com.ft.util.LoginUtil;
import com.ft.model.dto.StockLogDTO;
import com.ft.model.mdo.StockLogDO;
import com.ft.service.StockLogService;
import io.swagger.annotations.Api;
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
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@LoginCheck
	public String list(
			@RequestParam(value = "current_page") int currentPage,
			@RequestParam(value = "type", required = false, defaultValue = "0") short type,
			@RequestParam(value = "goods_id", required = false, defaultValue = "0") long goodsId,
			@RequestParam(value = "order_id", required = false) String orderId,
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

		return JsonUtil.object2Json(RestResult.getSuccessRestResult(stockLogService.list(stockLogDTO, pageParam)));
	}

	@ApiOperation("出/入库操作")
	@RequestMapping(value = "/storage", method = RequestMethod.POST)
	@LoginCheck
	public String storage(
			HttpServletRequest request,
			@RequestParam("type") short type,
			@RequestParam("goods_id") long goodsId,
			@RequestParam(value = "order_id", defaultValue = "0") String orderId,
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
		return JsonUtil.object2Json(RestResult.getSuccessRestResult(flag));
	}
}
