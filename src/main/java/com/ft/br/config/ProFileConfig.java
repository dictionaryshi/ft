package com.ft.br.config;

import com.ft.br.constant.PropertiesConstant;
import com.ft.web.model.UserDO;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;

/**
 * 多环境配置
 *
 * @author shichunyang
 */
@Configuration
public class ProFileConfig {
	/**
	 * 获取User对象
	 * Scope 多实例时, 启动容器时不会创建该对象
	 * Lazy 单实例时, 添加该注解, 启动容器不会创建该对象
	 *
	 * @return User对象
	 */
	@Bean(initMethod = "init", destroyMethod = "destroy")
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	@Lazy
	public UserDO scy() {
		UserDO user = new UserDO();
		user.setUsername("scy");
		return user;
	}

	@Bean
	public UserDO zgl() {
		UserDO user = new UserDO();
		user.setUsername("zgl");
		return user;
	}

	@Bean
	@ConfigurationProperties(prefix = "com.ft")
	public PropertiesConstant propertiesConstants() {
		return new PropertiesConstant();
	}
}
