package com.ft.config.mvc;

import com.ft.util.JsonUtil;
import com.ft.web.plugin.ControllerAspect;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

/**
 * MvcConfig
 *
 * @author shichunyang
 */
@Configuration
public class MvcConfig extends WebMvcConfigurationSupport {

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		// 设置是否是后缀模式匹配
		configurer.setUseSuffixPatternMatch(false)
				// 设置是否自动后缀路径模式匹配
				.setUseTrailingSlashMatch(true);

	}

	/**
	 * 对静态资源的配置
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String fe = "file:/data/web/";

		String webjars = "classpath:/META-INF/resources/webjars/";
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/", webjars, "classpath:/META-INF/resources/", fe);
		registry.addResourceHandler("/webjars/**").addResourceLocations(webjars);
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		if (CollectionUtils.isEmpty(converters)) {
			return;
		}

		converters.forEach(converter -> {
			if (converter instanceof MappingJackson2HttpMessageConverter) {
				MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) converter;
				mappingJackson2HttpMessageConverter.setObjectMapper(new JsonUtil.JsonMapper());
			} else if (converter instanceof StringHttpMessageConverter) {
				StringHttpMessageConverter stringHttpMessageConverter = (StringHttpMessageConverter) converter;
				// 去除响应中服务器可接收编码
				stringHttpMessageConverter.setWriteAcceptCharset(false);
				stringHttpMessageConverter.setDefaultCharset(Charset.forName("UTF-8"));
			}
		});
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins(CorsConfiguration.ALL).allowedMethods(CorsConfiguration.ALL).allowCredentials(Boolean.TRUE);
	}

	@Override
	public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		exceptionResolvers.clear();
		String charSet = "UTF-8";
		exceptionResolvers.add((request, response, handler, ex) -> {

			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

			try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), charSet))) {
				Object obj = ((ControllerAspect.ExceptionHandler) (httpServletRequest, httpServletResponse, throwable) -> null).handle(request, response, ex);
				String result;
				if (obj instanceof String) {
					result = (String) obj;
				} else {
					result = JsonUtil.object2Json(obj);
				}
				if (result == null) {
					result = "";
				}
				out.write(result);
			} catch (Exception e) {
			}
			return new ModelAndView();
		});
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new HandlerInterceptorAdapter() {
			@Override
			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
				disableIframe(response);
				return true;
			}

			private void disableIframe(HttpServletResponse response) {
				response.addHeader("X-Frame-Options", "DENY");
			}
		});
	}

	/**
	 * 开启WebSocket支持
	 */
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}
}
