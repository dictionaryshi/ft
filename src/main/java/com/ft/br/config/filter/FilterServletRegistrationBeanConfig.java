package com.ft.br.config.filter;

import com.ft.br.controller.DemoServlet;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * FilterServletRegistrationBeanConfig
 *
 * @author shichunyang
 */
@Configuration
public class FilterServletRegistrationBeanConfig {
	@Bean
	public ServletRegistrationBean demoServlet() {
		ServletRegistrationBean<DemoServlet> demoServlet = new ServletRegistrationBean<>(new DemoServlet(), "/demo/*");
		Map<String, String> parameters = new HashMap<>(16);
		parameters.put("username", "root");
		parameters.put("password", "一只飞的猪");
		demoServlet.setInitParameters(parameters);
		return demoServlet;
	}

	//	@Bean
	public FilterRegistrationBean characterEncodingFilter() {
		FilterRegistrationBean<CharacterEncodingFilter> characterEncodingFilter = new FilterRegistrationBean<>();
		characterEncodingFilter.setFilter(new CharacterEncodingFilter("UTF-8", true, true));

		Map<String, String> parameters = new HashMap<>(16);
		characterEncodingFilter.setInitParameters(parameters);

		characterEncodingFilter.setUrlPatterns(Collections.singletonList("/*"));
		return characterEncodingFilter;
	}
}
