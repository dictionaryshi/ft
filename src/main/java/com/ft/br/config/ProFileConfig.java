package com.ft.br.config;

import com.ft.br.service.impl.SpringLifeServiceImpl;

/**
 * 多环境配置
 *
 * @author shichunyang
 */
@Configuration
public class ProFileConfig {

    @Bean(initMethod = "init", destroyMethod = "customDestroy")
    public SpringLifeServiceImpl springLifeService() {
        return new SpringLifeServiceImpl("SpringLifeBean");
    }
}
