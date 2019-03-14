package com.ft.config.plugin;

import com.ft.db.plugin.DataSourceAspect;
import com.ft.redis.plugin.KafkaAop;
import com.ft.redis.plugin.RedisAop;
import com.ft.web.plugin.ControllerAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 插件集成
 *
 * @author shichunyang
 */
@Configuration
@Slf4j
public class PluginConfiguration {

	/**
	 * 登陆校验插件
	 */
	@Bean
	@ConditionalOnMissingBean(LoginAop.class)
	public LoginAop loginAop() {
		log.info("插件初始化==>LoginAop");
		return new LoginAop();
	}

	/**
	 * 数据源切换插件
	 */
	@Bean
	@ConditionalOnMissingBean(DataSourceAspect.class)
	public DataSourceAspect dataSourceAspect() {
		log.info("插件初始化==>DataSourceAspect");
		return new DataSourceAspect();
	}

	/**
	 * Redis插件
	 */
	@Bean
	@ConditionalOnMissingBean(RedisAop.class)
	public RedisAop redisAop() {
		log.info("插件初始化==>RedisAop");
		return new RedisAop();
	}

	/**
	 * 异常处理插件
	 */
	@Bean
	@ConditionalOnMissingBean(ControllerAspect.class)
	public ControllerAspect controllerAspect() {
		log.info("插件初始化==>ControllerAspect");
		return new ControllerAspect();
	}

	@Bean
	@ConditionalOnMissingBean(KafkaAop.class)
	public KafkaAop kafkaAop() {
		return new KafkaAop();
	}
}
