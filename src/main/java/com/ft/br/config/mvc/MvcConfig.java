package com.ft.br.config.mvc;

import com.ft.web.plugin.MailUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * MvcConfig
 *
 * @author shichunyang
 */
@Configuration
public class MvcConfig {
	/**
	 * 开启WebSocket支持
	 */
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

	@Bean
	public MailUtil mailUtil(JavaMailSender javaMailSender) {
		return new MailUtil(javaMailSender);
	}
}
