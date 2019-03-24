package com.ft.br.study.inherit;

/**
 * Son
 *
 * @author shichunyang
 */
public class Son extends Father {
	private String i = i();

	private static String j = j();

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

	@Override
	public String i() {
		String result = "Son 普通属性";
		System.out.println(result);
		return result;
	}

	public static String j() {
		String result = "Son 静态属性";
		System.out.println(result);
		return result;
	}

	public static void main(String[] args) {
		new Son();
	}
}
