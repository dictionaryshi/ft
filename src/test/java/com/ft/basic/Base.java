package com.ft.basic;

import java.lang.reflect.ParameterizedType;

/**
 * Base
 *
 * @author shichunyang
 */
public abstract class Base<T> {

	public Base() {
		// 获取子类传递的参数化类型
		Class beanClass = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

		System.out.println(beanClass.getName());
	}
}
