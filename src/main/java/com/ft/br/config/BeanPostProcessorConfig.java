package com.ft.br.config;

import com.ft.br.constant.OrderTypeEnum;
import com.ft.br.service.SpringLifeService;
import com.ft.util.plugin.BaseBeanPostProcessor;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.Configuration;

/**
 * BeanPostProcessorConfig
 *
 * @author shichunyang
 */
@Configuration
public class BeanPostProcessorConfig extends BaseBeanPostProcessor<SpringLifeService> {
	@Override
	protected void before(SpringLifeService bean, String beanName) throws BeansException {
		System.out.println("SpringLifeBean " + 4);
	}

	@Override
	protected void after(SpringLifeService springLifeService, String beanName) throws BeansException {
		OrderTypeEnum orderStatus = springLifeService.getStatus();
		System.out.println("SpringLifeBean " + 8);
	}
}
