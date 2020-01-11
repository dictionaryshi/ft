package com.ft.br.constant;

/**
 * RulePropertyTypeEnum
 *
 * @author shichunyang
 */
public enum RulePropertyTypeEnum {

	/**
	 * 集合
	 */
	NUMBER(1, "数值"),
	STRING(2, "字符串"),
	COLLECTION(3, "集合");

	private Integer type;

	private String message;

	RulePropertyTypeEnum(Integer type, String message) {
		this.type = type;
		this.message = message;
	}

	public Integer getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}
}
