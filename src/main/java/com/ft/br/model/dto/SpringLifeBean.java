package com.ft.br.model.dto;

import lombok.Data;

/**
 * spring 生命周期
 *
 * @author shichunyang
 */
@Data
public class SpringLifeBean {

	private String username;

	public void init() {
	}

	public void destroy() {
	}
}
