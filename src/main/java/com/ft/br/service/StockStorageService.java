package com.ft.br.service;

import com.ft.br.model.ao.stock.StockLogStorageAO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 出入库操作
 *
 * @author shichunyang
 */
public interface StockStorageService {

    Map<Integer, StockStorageService> STOCK_STORAGE_SERVICE_MAP = new ConcurrentHashMap<>();

    /**
     * 获取操作类型(1:入库, 2出库)
     *
     * @return 操作类型
     */
    int type();

    /**
     * 出入库操作
     *
     * @param stockLogStorageAO 操作参数
     * @return true 操作成功
     */
    boolean storage(StockLogStorageAO stockLogStorageAO);
}
