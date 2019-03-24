package com.ft.br.study.inherit;

/**
 * Grandfather
 *
 * @author shichunyang
 */
public class Grandfather {
	private String i = i();

	static {
		System.out.println("Grandfather静态代码块");
	}

	private static String j = j();

	{
		System.out.println("Grandfather构造代码块");
	}

	public Grandfather() {
		super();
		System.out.println("Grandfather默认构造方法");
	}

	public String i() {
		String result = "Grandfather 普通属性";
		System.out.println(result);
		return result;
	}

	public static String j() {
		String result = "Grandfather 静态属性";
		System.out.println(result);
		return result;
	}
}
