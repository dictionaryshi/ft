package com.ft.study.inherit;

/**
 * Grandfather
 *
 * @author shichunyang
 */
public class Grandfather {
	static {
		System.out.println("Grandfather静态代码块");
	}

	{
		System.out.println("Grandfather构造代码块");
	}

	public Grandfather() {
		super();
		System.out.println("Grandfather默认构造方法");
	}
}
