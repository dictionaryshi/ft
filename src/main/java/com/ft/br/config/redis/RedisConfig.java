package com.ft.br.config.redis;

import com.ft.redis.base.ValueOperationsCache;
import com.ft.redis.lock.RedisLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RedisConfig
 *
 * @author shichunyang
 */
@Configuration("com.ft.br.config.redis.RedisConfig")
public class RedisConfig {
	private ValueOperationsCache<String, String> valueOperationsCache;

	public RedisConfig(ValueOperationsCache<String, String> valueOperationsCache) {
		this.valueOperationsCache = valueOperationsCache;
	}

	@Bean("com.ft.redis.lock.RedisLock")
	public RedisLock redisLock() {
		return new RedisLock(valueOperationsCache);
	}
}
