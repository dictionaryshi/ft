package com.ft.br.service.impl;

import com.ft.br.constant.StockLogTypeDetailEnum;
import com.ft.br.constant.StockLogTypeEnum;
import com.ft.br.dao.GoodsMapper;
import com.ft.br.dao.StockLogMapper;
import com.ft.br.model.ao.stock.StockLogStorageAO;
import com.ft.br.service.GoodsService;
import com.ft.br.service.StockStorageService;
import com.ft.dao.stock.model.StockLogDO;
import com.ft.db.annotation.UseMaster;
import com.ft.db.constant.DbConstant;
import com.ft.util.exception.FtException;
import com.ft.util.model.LogAO;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private StockLogMapper stockLogMapper;

    @Autowired
    private GoodsService goodsService;

    @Override
    public int type() {
        return StockLogTypeEnum.OUT.getType();
    }

    @UseMaster
    @Transactional(value = DbConstant.DB_CONSIGN + DbConstant.TRAN_SACTION_MANAGER, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
    @Override
    public boolean storage(StockLogStorageAO stockLogStorageAO) {
        StockLogDO stockLogDO = new StockLogDO();
        stockLogDO.setOperator(stockLogStorageAO.getOperator());
        stockLogDO.setType(stockLogStorageAO.getType());
        stockLogDO.setTypeDetail(StockLogTypeDetailEnum.OUT_PERSON.getTypeDetail());

        int goodsId = stockLogStorageAO.getGoodsId();

        stockLogDO.setGoodsId(goodsId);

        int beforeStockNumber = goodsService.getStock(goodsId);

        stockLogDO.setBeforeStockNumber(beforeStockNumber);

        int goodsNumber = stockLogStorageAO.getStorageNumber();

        stockLogDO.setGoodsNumber(goodsNumber);

        if (goodsNumber > beforeStockNumber) {
            FtException.throwException("商品库存数量不足",
                    LogAO.build("goodsId", goodsId + ""),
                    LogAO.build("stockNumber", beforeStockNumber + ""));
        }

        // 操作库存
        goodsMapper.updateNumber(goodsId, -goodsNumber);

        stockLogDO.setAfterStockNumber(goodsService.getStock(goodsId));
        stockLogDO.setOrderId(stockLogStorageAO.getOrderId());
        stockLogDO.setRemark(stockLogStorageAO.getRemark());

        stockLogMapper.insertSelective(stockLogDO);

        return Boolean.TRUE;
    }
}
