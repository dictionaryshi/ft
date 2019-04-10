package com.ft.br.config;

import com.ft.br.constant.OrderTypeEnum;
import com.ft.br.service.SpringLifeService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanPostProcessorConfig implements BeanPostProcessor {
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (SpringLifeService.class.isAssignableFrom(bean.getClass())) {
			System.out.println("SpringLifeBean " + 4);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (SpringLifeService.class.isAssignableFrom(bean.getClass())) {
			SpringLifeService springLifeService = (SpringLifeService) bean;
			OrderTypeEnum orderStatus = springLifeService.getStatus();
			System.out.println("SpringLifeBean " + 8);
		}
		return bean;
	}
}
