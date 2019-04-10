package com.ft.br.model.dto;

import com.ft.br.service.SpringLifeService;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * spring 生命周期
 *
 * @author shichunyang
 */
@Data
public class SpringLifeBean implements SpringLifeService, BeanNameAware, BeanFactoryAware, ApplicationContextAware, InitializingBean, DisposableBean {
	private String username;

	@Override
	public void setBeanName(String name) {
		System.out.println(this.username + " " + 1);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println(this.username + " " + 2);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.out.println(this.username + " " + 3);
	}

	@PostConstruct
	public void postConstruct() {
		System.out.println(this.username + " " + 5);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println(this.username + " " + 6);
	}

	public void init() {
		System.out.println(this.username + " " + 7);
	}

	@PreDestroy
	public void preDestroy() {
		System.out.println(this.username + " " + 9);
	}

	@Override
	public void destroy() throws Exception {
		System.out.println(this.username + " " + 10);
	}

	public void customDestroy() {
		System.out.println(this.username + " " + 11);
	}
}
