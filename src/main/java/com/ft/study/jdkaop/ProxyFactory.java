package com.ft.study.jdkaop;

import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 代理工厂
 *
 * @author shichunyang
 */
@Data
public class ProxyFactory {

	/**
	 * 要增强的对象
	 */
	private Object targetObject;

	/**
	 * 前置增强内容
	 */
	private BeforeAdvice beforeAdvice;

	/**
	 * 后置增强内容
	 */
	private AfterAdvice afterAdvice;

	/**
	 * 创建代理对象
	 */
	public Object createProxy() {

		// 得到类加载器
		ClassLoader classLoader = this.getClass().getClassLoader();

		// 得到增强对象实现的接口
		Class[] classArray = targetObject.getClass().getInterfaces();

		//调用代理对象方法时会执行这里的内容
		InvocationHandler invocationHandler = (proxy, method, args) -> {

			// 前置增强
			if (beforeAdvice != null) {
				beforeAdvice.before();
			}

			Object result = method.invoke(targetObject, args);

			// 后置增强
			if (afterAdvice != null) {
				afterAdvice.after();
			}

			return result;
		};

		return Proxy.newProxyInstance(classLoader, classArray, invocationHandler);
	}
}
