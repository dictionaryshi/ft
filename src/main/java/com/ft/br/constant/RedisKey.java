package com.ft.br.constant;

/**
 * RedisKey
 *
 * @author shichunyang
 */
public class RedisKey {
	private RedisKey() {
	}

	/**
	 * 图片验证码redis key
	 */
	public static final String REDIS_VERIFICATION_CODE = "ft_br_verification_code";

	/**
	 * 登录token redis key
	 */
	public static final String REDIS_LOGIN_TOKEN = "ft_br_redis_token";
}
