package com.ft.br.study.inside;

/**
 * 外部类
 *
 * @author shichunyang
 */
public class Body {

	private int age = 30;

	private class Heart {

		private int age = 60;

		private void heartFunction() {
			System.out.println("外部类成员变量==>" + Body.this.age);
			System.out.println("内部类成员变量" + Heart.this.age);
		}

	}

	public void bodyFunction() {
		Heart heart = new Heart();
		heart.heartFunction();

		System.out.println("---");

		System.out.println("内部类私有变量==>" + heart.age);
	}

	/**
	 * 静态内部类
	 */
	public static class InnerClass {
		public static void show2() {
			System.out.println("InnerClass static method");
		}
	}

	public static void main(String[] args) {
		Body body = new Body();
		body.bodyFunction();

		Body.InnerClass.show2();
	}
}
