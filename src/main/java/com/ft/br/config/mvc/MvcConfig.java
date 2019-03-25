package com.ft.br.config.mvc;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
}