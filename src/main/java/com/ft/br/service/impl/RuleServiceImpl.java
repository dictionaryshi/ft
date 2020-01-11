package com.ft.br.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ft.br.model.bo.RuleBO;
import com.ft.br.service.RuleService;
import com.ft.util.JsonUtil;
import com.ft.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 规则引擎
 *
 * @author shichunyang
 */
@Slf4j
public class RuleServiceImpl implements RuleService {

	@Override
	public String valid(List<RuleBO> rules, String objJson) {
		Map<String, Object> objectMap = JsonUtil.json2Object(objJson, new TypeReference<Map<String, Object>>() {
		});
		if (objectMap == null) {
			return "对象json解析失败";
		}

		if (ObjectUtil.isEmpty(rules)) {
			return "未配置规则";
		}

		for (RuleBO rule : rules) {
			String errorMessage = this.validRule(rule, objectMap);
			if (errorMessage != null) {
				return errorMessage;
			}
		}

		return null;
	}

	private String validRule(RuleBO rule, Map<String, Object> objectMap) {
		return null;
	}
}
