package com.ft.br.service.impl;

import com.ft.br.constant.StockLogTypeDetailEnum;
import com.ft.br.constant.StockLogTypeEnum;
import com.ft.br.dao.OrderMapper;
import com.ft.br.dao.StockLogMapper;
import com.ft.br.model.ao.stock.StockLogListAO;
import com.ft.br.model.ao.stock.StockLogStorageAO;
import com.ft.br.model.bo.StockLogBO;
import com.ft.br.service.GoodsService;
import com.ft.br.service.StockLogService;
import com.ft.br.service.UserService;
import com.ft.dao.stock.model.GoodsDO;
import com.ft.dao.stock.model.OrderDO;
import com.ft.dao.stock.model.StockLogDO;
import com.ft.db.model.PageResult;
import com.ft.util.ObjectUtil;
import com.ft.util.exception.FtException;
import com.ft.util.model.LogAO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	private GoodsService goodsService;

	@Autowired
	private OrderMapper orderMapper;

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

	@Override
	public boolean check(StockLogStorageAO stockLogStorageAO) {
		int goodsId = stockLogStorageAO.getGoodsId();
		GoodsDO goodsDO = goodsService.get(goodsId);
		if (goodsDO == null) {
			FtException.throwException("商品不存在",
					LogAO.build("goodsId", goodsId + ""));
		}

		Long orderId = stockLogStorageAO.getOrderId();
		if (orderId != null) {
			OrderDO orderDO = orderMapper.selectByPrimaryKey(orderId);
			if (orderDO == null) {
				FtException.throwException("订单不存在",
						LogAO.build("orderId", orderId + ""));
			}
		}

		return Boolean.TRUE;
	}
}
