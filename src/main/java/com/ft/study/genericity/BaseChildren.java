package com.ft.study.genericity;

import com.ft.model.mdo.Employee;

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
