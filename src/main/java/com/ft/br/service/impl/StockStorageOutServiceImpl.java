package com.ft.br.service.impl;

import com.ft.br.constant.StockLogTypeEnum;
import com.ft.br.model.ao.stock.StockLogStorageAO;
import com.ft.br.service.StockStorageService;
import com.ft.db.annotation.UseMaster;
import com.ft.db.constant.DbConstant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 出库操作
 *
 * @author shichunyang
 */
@Service("com.ft.br.service.impl.StockStorageOutServiceImpl")
public class StockStorageOutServiceImpl implements StockStorageService {

	@Override
	public int type() {
		return StockLogTypeEnum.OUT.getType();
	}

	@UseMaster
	@Transactional(value = DbConstant.DB_CONSIGN + DbConstant.TRAN_SACTION_MANAGER, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	@Override
	public boolean storage(StockLogStorageAO stockLogStorageAO) {
		return Boolean.TRUE;
	}
}
