package com.ft.br.config.swagger;

import com.ft.util.SpringContextUtil;
import com.ft.web.util.SwaggerUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * SwaggerConfig
 *
 * @author shichunyang
 */
@Profile(value = {SpringContextUtil.ENV_DEV, SpringContextUtil.ENV_STAG})
@Configuration
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
