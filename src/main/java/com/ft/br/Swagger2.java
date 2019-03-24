package com.ft.br;

import com.ft.br.constant.LoginConstant;
import com.ft.web.constant.SwaggerConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger2
 *
 * @author shichunyang
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
	@Bean
	public Docket createRestApi() {

		// 添加token header
		ParameterBuilder parameterBuilder = new ParameterBuilder();
		parameterBuilder.name(LoginConstant.PARAM_LOGIN_TOKEN).description("登陆token").modelRef(new ModelRef(SwaggerConstant.DATA_TYPE_STRING)).parameterType(SwaggerConstant.PARAM_TYPE_HEADER).required(false).build();

		List<Parameter> parameters = new ArrayList<>();
		parameters.add(parameterBuilder.build());

		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("wx")
				.useDefaultResponseMessages(false)
				.genericModelSubstitutes(ResponseEntity.class)
				.forCodeGeneration(true)
				// 项目名称
				.pathMapping("/")
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.ft"))
				.paths(PathSelectors.regex("/api.*"))
				.build()
				.globalOperationParameters(parameters);
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("温馨家具API")
				.description("前后端分离参考文档")
				.termsOfServiceUrl("http://www.baidu.com")
				.contact(new Contact("史春阳", "http://www.google.com", "903031015@qq.com"))
				.version("V1.0")
				.build();
	}
}
