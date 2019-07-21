package com.ft.br.service;

import com.ft.br.model.ao.LoginAO;
import com.ft.br.model.bo.CodeBO;
import com.ft.br.model.bo.TokenBO;

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

	/**
	 * 登陆
	 *
	 * @param loginAO 登录录入数据
	 * @return sso token
	 */
	TokenBO login(LoginAO loginAO);
}
