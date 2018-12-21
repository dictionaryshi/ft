package com.ft.study.genericity;

import java.lang.reflect.ParameterizedType;

/**
 * BaseParent
 *
 * @author shichunyang
 */
public class BaseParent<T> {
	public BaseParent() {
		// 获取子类传递的参数化类型
		Class beanClass = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		System.out.println(beanClass.getName());
	}
}
