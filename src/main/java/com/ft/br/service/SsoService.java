package com.ft.br.service;

import com.ft.br.model.bo.CodeBO;

/**
 * SsoService
 *
 * @author shichunyang
 */
public interface SsoService {
	/**
	 * 获取图片验证码
	 *
	 * @return 图片验证码
	 */
	CodeBO getCode();
}
