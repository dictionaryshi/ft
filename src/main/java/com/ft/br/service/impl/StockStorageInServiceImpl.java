package com.ft.br.service.impl;

import com.ft.br.constant.StockLogTypeEnum;
import com.ft.br.dao.GoodsMapper;
import com.ft.br.dao.StockLogMapper;
import com.ft.br.model.ao.stock.StockLogStorageAO;
import com.ft.br.service.StockStorageService;
import com.ft.db.annotation.UseMaster;
import com.ft.db.constant.DbConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 入库操作
 *
 * @author shichunyang
 */
@Service("com.ft.br.service.impl.StockStorageInServiceImpl")
public class StockStorageInServiceImpl implements StockStorageService {

	@Autowired
	private GoodsMapper goodsMapper;

	@Autowired
	private StockLogMapper stockLogMapper;

	@Override
	public int type() {
		return StockLogTypeEnum.IN.getType();
	}

	@UseMaster
	@Transactional(value = DbConstant.DB_CONSIGN + DbConstant.TRAN_SACTION_MANAGER, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public boolean storage(StockLogStorageAO stockLogStorageAO) {
		return Boolean.TRUE;
	}
}
