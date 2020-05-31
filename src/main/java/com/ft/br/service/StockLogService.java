package com.ft.br.service;

import com.ft.br.model.ao.stock.StockLogListAO;
import com.ft.br.model.ao.stock.StockLogStorageAO;
import com.ft.br.model.bo.StockLogBO;
import com.ft.db.model.PageResult;

/**
 * StockLogService
 *
 * @author shichunyang
 */
public interface StockLogService {
    /**
     * 分页查询仓库操作记录
     *
     * @param stockLogListAO 分页条件
     * @return 仓库操作记录
     */
    PageResult<StockLogBO> listByPage(StockLogListAO stockLogListAO);

    /**
     * 校验出入库参数
     *
     * @param stockLogStorageAO 出入库参数
     * @return true 校验成功
     */
    boolean check(StockLogStorageAO stockLogStorageAO);
}
