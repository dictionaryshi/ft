package com.ft.study.algorithm;

/**
 * 操作数
 *
 * @author shichunyang
 */
public class Operand {
	public static void main(String[] args) {
		int i = 1;
		i = i++;
		int j = i++;
		int k = i + ++i * i++;
		System.out.println("i=>" + i);
		System.out.println("j=>" + j);
		System.out.println("k=>" + k);
	}
}
