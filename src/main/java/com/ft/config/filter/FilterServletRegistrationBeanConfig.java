package com.ft.config.filter;

import com.alibaba.druid.support.http.ResourceServlet;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
	@Bean("statViewServlet")
	public ServletRegistrationBean statViewServlet() {
		ServletRegistrationBean<StatViewServlet> statViewServlet = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
		Map<String, String> parameters = new HashMap<>(16);
		parameters.put(ResourceServlet.PARAM_NAME_USERNAME, "root");
		parameters.put(ResourceServlet.PARAM_NAME_PASSWORD, "naodian12300");
		statViewServlet.setInitParameters(parameters);
		return statViewServlet;
	}

	@Bean("webStatFilter")
	public FilterRegistrationBean webStatFilter() {
		FilterRegistrationBean<WebStatFilter> webStatFilter = new FilterRegistrationBean<>();
		webStatFilter.setFilter(new WebStatFilter());

		Map<String, String> parameters = new HashMap<>(16);
		parameters.put(WebStatFilter.PARAM_NAME_EXCLUSIONS, "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		webStatFilter.setInitParameters(parameters);

		webStatFilter.setUrlPatterns(Collections.singletonList("/*"));
		return webStatFilter;
	}

	@Bean
	public ServletRegistrationBean hystrixMetricsStreamServlet() {
		ServletRegistrationBean<HystrixMetricsStreamServlet> registrationBean = new ServletRegistrationBean<>(new HystrixMetricsStreamServlet());
		registrationBean.addUrlMappings("/actuator/hystrix.stream");
		return registrationBean;
	}
}
