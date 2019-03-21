package com.ft.config.plugin;

import com.ft.redis.plugin.KafkaAop;
import com.ft.redis.plugin.RedisAop;
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
	 * Redis插件
	 */
	@Bean
	@ConditionalOnMissingBean(RedisAop.class)
	public RedisAop redisAop() {
		log.info("插件初始化==>RedisAop");
		return new RedisAop();
	}

	@Bean
	@ConditionalOnMissingBean(KafkaAop.class)
	public KafkaAop kafkaAop() {
		return new KafkaAop();
	}
}
