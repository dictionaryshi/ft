package com.ft.br.service.impl;

import com.ft.br.constant.StockLogTypeEnum;
import com.ft.br.model.ao.stock.StockLogStorageAO;
import com.ft.br.service.StockStorageService;
import org.springframework.stereotype.Service;

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

	@Override
	public boolean storage(StockLogStorageAO stockLogStorageAO) {
		return Boolean.TRUE;
	}
}
