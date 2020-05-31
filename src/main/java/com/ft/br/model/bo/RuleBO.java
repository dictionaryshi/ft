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
    private String inErrorMessage;

    /**
     * 不包含
     */
    private List<Object> ni;
    private String niErrorMessage;

    /**
     * 等于
     */
    private Object eq;
    private String eqErrorMessage;

    /**
     * 不等于
     */
    private Object ne;
    private String neErrorMessage;

    /**
     * 大于
     */
    private Number gt;
    private String gtErrorMessage;

    /**
     * 小于
     */
    private Number lt;
    private String ltErrorMessage;

    /**
     * 大于等于
     */
    private Number ge;
    private String geErrorMessage;

    /**
     * 小于等于
     */
    private Number le;
    private String leErrorMessage;

    /**
     * 或者
     */
    private List<RuleBO> or;

    /**
     * 并且
     */
    private List<RuleBO> and;
}
