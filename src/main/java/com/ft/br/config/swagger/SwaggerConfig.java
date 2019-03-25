package com.ft.br.config.swagger;

import com.ft.web.util.SwaggerUtil;
import com.ft.web.util.WebUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SwaggerConfig
 *
 * @author shichunyang
 */
@Profile(value = {WebUtil.ENV_DEV, WebUtil.ENV_STAG})
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket createRestApi() {
		return SwaggerUtil.docket(
				SwaggerUtil.apiInfo("温馨家具API", "前后端分离参考文档", "http://www.baidu.com", "史春阳", "http://www.google.com", "903031015@qq.com", "V1.0"),
				"wx",
				PathSelectors.regex("/api.*")
		);
	}
}
