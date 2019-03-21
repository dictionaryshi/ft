package com.ft.config.plugin;

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
}
