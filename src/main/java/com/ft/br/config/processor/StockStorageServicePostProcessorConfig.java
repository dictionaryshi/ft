package com.ft.br.config.processor;

import com.ft.br.service.StockStorageService;
import com.ft.util.plugin.BaseBeanPostProcessor;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.Configuration;

/**
 * 出入库策略
 *
 * @author shichunyang
 */
@Configuration
public class StockStorageServicePostProcessorConfig extends BaseBeanPostProcessor<StockStorageService> {

    @Override
    public void after(StockStorageService bean, String beanName) throws BeansException {
        StockStorageService.STOCK_STORAGE_SERVICE_MAP.put(bean.type(), bean);
    }
}
