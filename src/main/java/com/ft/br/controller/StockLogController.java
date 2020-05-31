package com.ft.br.controller;

import com.ft.br.constant.RedisKey;
import com.ft.br.model.ao.stock.StockLogListAO;
import com.ft.br.model.ao.stock.StockLogStorageAO;
import com.ft.br.model.bo.StockLogBO;
import com.ft.br.service.StockLogService;
import com.ft.br.service.StockStorageService;
import com.ft.db.annotation.PageParamCheck;
import com.ft.db.model.PageResult;
import com.ft.redis.lock.RedisLock;
import com.ft.redis.util.RedisUtil;
import com.ft.util.exception.FtException;
import com.ft.util.model.RestResult;
import com.ft.web.annotation.LoginCheck;
import com.ft.web.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private RedisLock redisLock;

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
    @LoginCheck
    @PostMapping("/storage")
    public RestResult<Boolean> storage(
            @RequestBody @Valid StockLogStorageAO stockLogStorageAO
    ) {
        int operator = WebUtil.getCurrentUser().getId();
        stockLogStorageAO.setOperator(operator);

        stockLogService.check(stockLogStorageAO);

        int type = stockLogStorageAO.getType();
        StockStorageService stockStorageService = StockStorageService.STOCK_STORAGE_SERVICE_MAP.get(type);
        if (stockStorageService == null) {
            FtException.throwException("操作类型不正确",
                    "type", type + "");
        }

        String lockKey = RedisUtil.getRedisKey(RedisKey.REDIS_GOODS_UPDATE_LOCK, stockLogStorageAO.getGoodsId() + "");
        try {
            redisLock.lock(lockKey);

            boolean result = stockStorageService.storage(stockLogStorageAO);

            return RestResult.success(result);
        } finally {
            redisLock.unlock(lockKey);
        }
    }
}
