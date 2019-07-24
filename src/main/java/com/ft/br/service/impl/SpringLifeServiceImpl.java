package com.ft.br.service.impl;

import com.ft.br.constant.OrderStatusEnum;
import com.ft.br.service.SpringLifeService;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SpringLifeServiceImpl implements SpringLifeService, BeanNameAware, BeanFactoryAware, ApplicationContextAware, InitializingBean, DisposableBean {
	private String username;

	@Autowired
	private OrderServiceImpl orderService;

	public SpringLifeServiceImpl(String username) {
		this.username = username;
		System.out.println(this.username + " " + "构造方法初始化");
	}

	@Override
	public void setBeanName(String name) {
		System.out.println(this.username + " " + "Autowired结束, orderService==>" + orderService);
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

	@Override
	public int getStatus() {
		return OrderStatusEnum.SUCCESS.getStatus();
	}
}
