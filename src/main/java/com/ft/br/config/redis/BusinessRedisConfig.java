package com.ft.br.config.redis;

import com.ft.redis.base.HashOperationsCache;
import com.ft.redis.base.ListOperationsCache;
import com.ft.redis.base.SetOperationsCache;
import com.ft.redis.base.ValueOperationsCache;
import com.ft.redis.lock.RedisLock;
import com.ft.redis.model.RedisDO;
import com.ft.redis.util.RedisUtil;
import com.ft.util.JsonUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * BusinessRedisConfig
 *
 * @author shichunyang
 */
@Configuration
public class BusinessRedisConfig {

	@Bean("redisSetting")
	@ConfigurationProperties(prefix = "redis")
	public RedisDO redisDO() {
		return new RedisDO();
	}

	@Bean("jedisPoolConfig")
	public JedisPoolConfig getJedisPoolConfig(@Qualifier("redisSetting") RedisDO redisSetting) {
		return RedisUtil.getJedisPoolConfig(redisSetting.getMinIdle(), redisSetting.getMaxIdle(), redisSetting.getMaxTotal());
	}

	@Bean("jedisConnectionFactory")
	public JedisConnectionFactory getJedisConnectionFactory(@Qualifier("jedisPoolConfig") JedisPoolConfig jedisPoolConfig, @Qualifier("redisSetting") RedisDO redisSetting) {
		return RedisUtil.getJedisConnectionFactory(jedisPoolConfig, redisSetting.getHostName(), redisSetting.getPort(), redisSetting.getPassword(), redisSetting.getDatabase());
	}

	@Bean("stringRedisSerializer")
	public StringRedisSerializer getStringRedisSerializer() {
		return new StringRedisSerializer();
	}

	@Bean("genericJackson2JsonRedisSerializer")
	public GenericJackson2JsonRedisSerializer getGenericJackson2JsonRedisSerializer() {
		return new GenericJackson2JsonRedisSerializer(new JsonUtil.JsonMapper());
	}

	@Bean("redisTemplate")
	public RedisTemplate<String, String> getRedisTemplate(
			@Qualifier("stringRedisSerializer") StringRedisSerializer stringRedisSerializer,
			@Qualifier("genericJackson2JsonRedisSerializer") GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer,
			@Qualifier("jedisConnectionFactory") JedisConnectionFactory jedisConnectionFactory

	) {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

		redisTemplate.setConnectionFactory(jedisConnectionFactory);
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setValueSerializer(stringRedisSerializer);
		redisTemplate.setHashKeySerializer(stringRedisSerializer);
		redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);

		return redisTemplate;
	}

	@Bean("valueOperationsCache")
	public ValueOperationsCache getValueOperationsCache(@Qualifier("redisTemplate") RedisTemplate<String, String> redisTemplate) {
		return new ValueOperationsCache(redisTemplate);
	}

	@Bean("listOperationsCache")
	public ListOperationsCache getListOperationsCache(@Qualifier("redisTemplate") RedisTemplate<String, String> redisTemplate) {
		return new ListOperationsCache(redisTemplate);
	}

	@Bean("setOperationsCache")
	public SetOperationsCache getSetOperationsCache(@Qualifier("redisTemplate") RedisTemplate<String, String> redisTemplate) {
		return new SetOperationsCache(redisTemplate);
	}

	@Bean("hashOperationsCache")
	public HashOperationsCache getHashOperationsCache(@Qualifier("redisTemplate") RedisTemplate<String, String> redisTemplate) {
		return new HashOperationsCache(redisTemplate);
	}

	@Bean
	public RedisLock redisLock(@Qualifier("valueOperationsCache") ValueOperationsCache valueOperationsCache) {
		return new RedisLock(valueOperationsCache);
	}
}
