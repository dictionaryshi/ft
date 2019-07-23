package com.ft.br.config.processor;

import com.ft.br.service.SpringLifeService;
import com.ft.util.plugin.BaseBeanPostProcessor;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.Configuration;

/**
 * BeanPostProcessorConfig
 *
 * @author shichunyang
 */
@Configuration("com.ft.br.config.processor.BeanPostProcessorConfig")
public class BeanPostProcessorConfig extends BaseBeanPostProcessor<SpringLifeService> {
	@Override
	protected void before(SpringLifeService bean, String beanName) throws BeansException {
		System.out.println("SpringLifeBean " + 4);
	}

	@Override
	protected void after(SpringLifeService springLifeService, String beanName) throws BeansException {
		int orderStatus = springLifeService.getStatus();
		System.out.println("SpringLifeBean " + 8);
	}
}
