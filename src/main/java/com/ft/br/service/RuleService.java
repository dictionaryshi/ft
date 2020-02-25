package com.ft.br.service;

import com.ft.br.model.bo.RuleBO;

/**
 * 规则引擎
 *
 * @author shichunyang
 */
public interface RuleService {

	/**
	 * 对对象json进行规则校验
	 *
	 * @param rule    规则
	 * @param objJson 对象json
	 * @return 错误信息
	 */
	String valid(RuleBO rule, String objJson);
}
