package com.ft.br.service;

import com.ft.br.model.bo.RuleBO;

import java.util.List;

public interface RuleService {

	/**
	 * 对对象json进行规则校验
	 *
	 * @param rules   规则
	 * @param objJson 对象json
	 * @return 错误信息
	 */
	String valid(List<RuleBO> rules, String objJson);
}
