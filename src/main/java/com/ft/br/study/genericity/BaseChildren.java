package com.ft.br.study.genericity;

import com.ft.br.model.mdo.Employee;

/**
 * BaseChildren
 *
 * @author shichunyang
 */
public class BaseChildren extends BaseParent<Employee> {
	public static void main(String[] args) {
		new BaseChildren();
	}
}
