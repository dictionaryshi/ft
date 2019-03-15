package com.ft.java8;

import com.ft.util.CollectionUtil;
import com.ft.util.JsonUtil;
import com.ft.util.StringUtil;
import com.ft.web.exception.FtException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GuavaTest {
	@Test
	public void charTest() {
		char[] chs = new char[2];
		System.out.println('\u0000' == chs[0]);
	}

	@Test
	public void joiner() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, null, 6);
		System.out.println(StringUtil.join(list, ","));
	}

	@Test
	public void comma() {
		Pattern pattern = StringUtil.COMMA_PATTERN;
		List<Integer> numbers = StringUtil.split(",1，,，  2,，，,  3，,", pattern)
				.stream().map(Integer::parseInt).collect(Collectors.toList());
		numbers.forEach(System.out::println);
	}

	@Test
	public void disjoint() {
		List<String> numberList1 = new ArrayList<>();
		numberList1.add("1");
		numberList1.add("2");
		numberList1.add("3");
		List<String> numberList2 = new ArrayList<>();
		numberList2.add("4");
		numberList2.add("5");
		numberList2.add("6");
		// 集合无交集返回true
		boolean flag = CollectionUtil.disjoint(numberList1, numberList2);
		Assert.assertTrue(flag);
	}

	@Test
	public void partition() {
		List<String> chars = new ArrayList<>();
		chars.add("a");
		chars.add("b");
		chars.add("c");
		chars.add("d");
		chars.add("e");
		chars.add("f");
		chars.add("g");
		// 将list分组, 每组3个元素
		List<List<String>> partitions = CollectionUtil.partition(chars, 3);
		System.out.println(JsonUtil.object2Json(partitions));
	}

	@Test
	public void assertTest() {
		FtException.assertCheck(true, "参数必须为true");
	}
}
