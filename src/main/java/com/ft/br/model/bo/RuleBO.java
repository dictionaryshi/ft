package com.ft.br.model.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * RuleBO
 *
 * @author shichunyang
 */
@Getter
@Setter
@ToString
public class RuleBO {

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 校验属性名称
	 */
	private String propertyName;

	/**
	 * 属性类型(1:数值, 2:字符串, 3:集合)
	 */
	private Integer propertyType;

	/**
	 * 包含
	 */
	private List<Object> in;

	/**
	 * 不包含
	 */
	private List<Object> ni;

	/**
	 * 等于
	 */
	private Object eq;

	/**
	 * 不等于
	 */
	private Object ne;

	/**
	 * 大于
	 */
	private Number gt;

	/**
	 * 小于
	 */
	private Number lt;

	/**
	 * 大于等于
	 */
	private Number ge;

	/**
	 * 小于等于
	 */
	private Number le;

	/**
	 * 或者
	 */
	private List<RuleBO> or;

	/**
	 * 并且
	 */
	private List<RuleBO> and;
}
