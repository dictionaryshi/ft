package com.ft.br.config.mvc;

import com.ft.redis.plugin.RedisWarning;
import com.ft.util.service.CommonService;
import com.ft.web.plugin.MailUtil;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.unit.DataSize;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.MultipartConfigElement;

/**
 * MvcConfig
 *
 * @author shichunyang
 */
@Configuration
public class MvcConfig {
	/**
	 * 文件上传配置
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 单个文件最大
		factory.setMaxFileSize(DataSize.parse("5MB"));
		/// 设置总上传数据总大小
		factory.setMaxRequestSize(DataSize.parse("20MB"));
		return factory.createMultipartConfig();
	}

	/**
	 * 开启WebSocket支持
	 */
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

	@Bean
	public RedisWarning redisWarning() {
		return new RedisWarning();
	}

	@Bean
	public CommonService commonService(RedisWarning redisWarning) {
		return new CommonService() {
			@Override
			public void work(String application) {
				redisWarning.flow(application);
			}
		};
	}

	@Bean
	public MailUtil mailUtil(JavaMailSender javaMailSender) {
		return new MailUtil(javaMailSender);
	}
}
