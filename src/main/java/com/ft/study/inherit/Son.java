package com.ft.study.inherit;

/**
 * Son
 *
 * @author shichunyang
 */
public class Son extends Father {
	static {
		System.out.println("Son静态代码块");
	}

	{
		System.out.println("Son构造代码块");
	}

	public Son() {
		super();
		System.out.println("Son默认构造方法");
	}

	public static void main(String[] args) {
		new Son();
	}
}
