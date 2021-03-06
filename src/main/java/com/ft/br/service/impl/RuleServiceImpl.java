package com.ft.br.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ft.br.constant.RulePropertyTypeEnum;
import com.ft.br.model.bo.RuleBO;
import com.ft.br.service.RuleService;
import com.ft.util.CollectionUtil;
import com.ft.util.JsonUtil;
import com.ft.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 规则引擎
 *
 * @author shichunyang
 */
@Slf4j
public class RuleServiceImpl implements RuleService {

    @Override
    public String valid(RuleBO rule, String objJson) {
        Map<String, Object> objectMap = JsonUtil.json2Object(objJson, new TypeReference<Map<String, Object>>() {
        });
        if (objectMap == null) {
            return "对象json解析失败, json=>" + objJson;
        }

        if (rule == null) {
            return "未配置校验规则";
        }

        return this.validRule(rule, objectMap);
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
        @SuppressWarnings("unchecked")
        Collection<Object> collection = (Collection<Object>) objectMap.get(rule.getPropertyName());
        if (collection == null) {
            return "对象不存在该属性, property=>" + rule.getPropertyName();
        }

        List<Object> in = rule.getIn();
        if (!ObjectUtil.isEmpty(in)) {
            if (!collection.containsAll(in)) {
                String errorMessage = this.or(rule, objectMap, rule.getInErrorMessage());
                if (errorMessage != null) {
                    return errorMessage;
                }
            }
        }

        List<Object> ni = rule.getNi();
        if (!ObjectUtil.isEmpty(ni)) {
            if (!CollectionUtil.disjoint(collection, ni)) {
                String errorMessage = this.or(rule, objectMap, rule.getNiErrorMessage());
                if (errorMessage != null) {
                    return errorMessage;
                }
            }
        }

        return null;
    }

    private String validRuleString(RuleBO rule, Map<String, Object> objectMap) {
        String objectValue = (String) objectMap.get(rule.getPropertyName());
        if (objectValue == null) {
            return "对象不存在该属性, property=>" + rule.getPropertyName();
        }

        return this.validRuleObject(rule, objectMap, objectValue);
    }

    private String validRuleObject(RuleBO rule, Map<String, Object> objectMap, Object objectValue) {
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

        Object ne = rule.getNe();
        if (ne != null) {
            if (ObjectUtil.equals(ne, objectValue)) {
                String errorMessage = this.or(rule, objectMap, rule.getNeErrorMessage());
                if (errorMessage != null) {
                    return errorMessage;
                }
            }
        }

        return null;
    }

    private String validRuleNumber(RuleBO rule, Map<String, Object> objectMap) {
        Number objectValue = (Number) objectMap.get(rule.getPropertyName());
        if (objectValue == null) {
            return "对象不存在该属性, property=>" + rule.getPropertyName();
        }

        String error = this.validRuleObject(rule, objectMap, objectValue);
        if (error != null) {
            return error;
        }

        Number gt = rule.getGt();
        if (gt != null) {
            if (objectValue.doubleValue() <= gt.doubleValue()) {
                String errorMessage = this.or(rule, objectMap, rule.getGtErrorMessage());
                if (errorMessage != null) {
                    return errorMessage;
                }
            }
        }

        Number lt = rule.getLt();
        if (lt != null) {
            if (objectValue.doubleValue() >= lt.doubleValue()) {
                String errorMessage = this.or(rule, objectMap, rule.getLtErrorMessage());
                if (errorMessage != null) {
                    return errorMessage;
                }
            }
        }

        Number ge = rule.getGe();
        if (ge != null) {
            if (objectValue.doubleValue() < ge.doubleValue()) {
                String errorMessage = this.or(rule, objectMap, rule.getGeErrorMessage());
                if (errorMessage != null) {
                    return errorMessage;
                }
            }
        }

        Number le = rule.getLe();
        if (le != null) {
            if (objectValue.doubleValue() > le.doubleValue()) {
                String errorMessage = this.or(rule, objectMap, rule.getLeErrorMessage());
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
}
