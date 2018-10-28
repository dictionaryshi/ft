package com.ft.config.cloud.feign;

import com.ft.web.cloud.feign.FeignRequestInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FeignConfig
 *
 * @author shichunyang
 */
@Configuration
public class FeignConfig {

	@Bean
	public RequestInterceptor requestInterceptor() {
		return new FeignRequestInterceptor();
	}
}
