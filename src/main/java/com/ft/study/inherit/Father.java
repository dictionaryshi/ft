package com.ft.study.inherit;

/**
 * Father
 *
 * @author shichunyang
 */
public class Father extends Grandfather {

	static {
		System.out.println("Father静态代码块");
	}

	{
		System.out.println("Father构造代码块");
	}

	public Father() {
		super();
		System.out.println("Father默认构造方法");
	}
}
