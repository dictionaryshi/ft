package com.ft.study.inherit;

/**
 * Father
 *
 * @author shichunyang
 */
public class Father extends Grandfather {

	private static String j = j();

	static {
		System.out.println("Father静态代码块");
	}

	{
		System.out.println("Father构造代码块");
	}

	private String i = i();

	public Father() {
		super();
		System.out.println("Father默认构造方法");
	}

	@Override
	public String i() {
		String result = "Father 普通属性";
		System.out.println(result);
		return result;
	}

	public static String j() {
		String result = "Father 静态属性";
		System.out.println(result);
		return result;
	}
}
