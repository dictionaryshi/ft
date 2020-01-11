package com.ft.br.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ft.br.constant.RulePropertyTypeEnum;
import com.ft.br.model.bo.RuleBO;
import com.ft.br.service.RuleService;
import com.ft.util.JsonUtil;
import com.ft.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
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
			return "对象json解析失败, json=>" + objJson;
		}

		if (ObjectUtil.isEmpty(rules)) {
			return "未配置校验规则";
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
		int propertyType = rule.getPropertyType();
		if (propertyType == RulePropertyTypeEnum.NUMBER.getType()) {
			return this.validRuleNumber(rule, objectMap);
		} else if (propertyType == RulePropertyTypeEnum.STRING.getType()) {
			return this.validRuleString(rule, objectMap);
		} else if (propertyType == RulePropertyTypeEnum.COLLECTION.getType()) {
			return this.validRuleCollection(rule, objectMap);
		}

		return "校验规则属性类型配置错误";
	}

	private String validRuleCollection(RuleBO rule, Map<String, Object> objectMap) {
		return null;
	}

	private String validRuleString(RuleBO rule, Map<String, Object> objectMap) {
		return null;
	}

	private String validRuleNumber(RuleBO rule, Map<String, Object> objectMap) {
		Number objectValue = (Number) objectMap.get(rule.getPropertyName());
		if (objectValue == null) {
			return "对象不存在该属性, property=>" + rule.getPropertyName();
		}

		List<Object> targetValues = rule.getIn();
		if (!ObjectUtil.isEmpty(targetValues)) {
			if (!targetValues.contains(objectValue)) {
				String errorMessage = this.or(rule, objectMap, rule.getInErrorMessage());
				if (errorMessage != null) {
					return errorMessage;
				}
			}
		}

		List<Object> ni = rule.getNi();
		if (!ObjectUtil.isEmpty(ni)) {
			if (ni.contains(objectValue)) {
				String errorMessage = this.or(rule, objectMap, rule.getNiErrorMessage());
				if (errorMessage != null) {
					return errorMessage;
				}
			}
		}

		Object eq = rule.getEq();
		if (eq != null) {
			if (!ObjectUtil.equals(eq, objectValue)) {
				String errorMessage = this.or(rule, objectMap, rule.getEqErrorMessage());
				if (errorMessage != null) {
					return errorMessage;
				}
			}
		}

		List<RuleBO> ands = rule.getAnd();
		if (!ObjectUtil.isEmpty(ands)) {
			// 所有条件已通过, 那么校验and逻辑
			for (RuleBO and : ands) {
				String errorMessage = validRule(and, objectMap);
				if (errorMessage != null) {
					return errorMessage;
				}
			}
		}

		return null;
	}

	private String or(RuleBO rule, Map<String, Object> objectMap, String firstErrorMessage) {
		StringBuilder sb = new StringBuilder(firstErrorMessage);

		List<RuleBO> ors = rule.getOr();
		if (!ObjectUtil.isEmpty(ors)) {
			for (RuleBO or : ors) {
				String errorMessage = validRule(or, objectMap);
				if (errorMessage == null) {
					return null;
				} else {
					sb.append(",").append(errorMessage);
				}
			}
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		RuleService ruleService = new RuleServiceImpl();

		System.out.println(ruleService.valid(null, "ab"));
		System.out.println(ruleService.valid(null, "{}"));

		List<RuleBO> rules = new ArrayList<>();
		RuleBO ruleBO = new RuleBO();
		ruleBO.setPropertyType(0);
		rules.add(ruleBO);
		System.out.println(ruleService.valid(rules, "{}"));

		rules = new ArrayList<>();
		ruleBO = new RuleBO();
		ruleBO.setPropertyType(1);
		ruleBO.setPropertyName("price");
		rules.add(ruleBO);
		System.out.println(ruleService.valid(rules, "{}"));

		rules = new ArrayList<>();
		ruleBO = new RuleBO();
		ruleBO.setDescription("价格规则校验");
		ruleBO.setPropertyType(1);
		ruleBO.setPropertyName("price");
		ruleBO.setIn(Arrays.asList(30000, 40000, 60000));
		ruleBO.setInErrorMessage("指定价格不存在");
		List<RuleBO> ors = new ArrayList<>();
		RuleBO or = new RuleBO();
		or.setPropertyType(1);
		or.setPropertyName("price");
		or.setIn(Arrays.asList(30000, 40000, 60000));
		or.setInErrorMessage("指定价格又不存在");
		ors.add(or);
		ruleBO.setOr(ors);
		rules.add(ruleBO);
		System.out.println(ruleService.valid(rules, "{\"price\":50000}"));

		rules = new ArrayList<>();
		ruleBO = new RuleBO();
		ruleBO.setPropertyType(1);
		ruleBO.setPropertyName("price");
		ruleBO.setNi(Arrays.asList(50000, 60000));
		ruleBO.setNiErrorMessage("不能包含特定价格");
		rules.add(ruleBO);
		System.out.println(ruleService.valid(rules, "{\"price\":50000}"));

		rules = new ArrayList<>();
		ruleBO = new RuleBO();
		ruleBO.setPropertyType(1);
		ruleBO.setPropertyName("price");
		ruleBO.setEq(40000);
		ruleBO.setEqErrorMessage("不等于指定价格");
		rules.add(ruleBO);
		System.out.println(ruleService.valid(rules, "{\"price\":50000}"));
	}
}
