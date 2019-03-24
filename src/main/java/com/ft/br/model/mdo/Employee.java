package com.ft.br.model.mdo;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * 员工类
 *
 * @author shichunyang
 */
@Data
public class Employee {
	private String name;
	private Integer age;

	public Employee(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public Employee() {
	}

	public static final List<Employee> LIST = Arrays.asList(
			new Employee("张三", 30),
			new Employee("李四", 20),
			new Employee("王五", 10),
			new Employee("赵六", 50),
			new Employee("田七", 40)
	);
}
