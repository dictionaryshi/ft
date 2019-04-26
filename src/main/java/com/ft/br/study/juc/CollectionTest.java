package com.ft.br.study.juc;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class CollectionTest {
	public static void main(String[] args) {
		CopyOnWriteArrayList<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
		boolean flag1 = copyOnWriteArrayList.addIfAbsent(1);
		boolean flag2 = copyOnWriteArrayList.addIfAbsent(1);

		CopyOnWriteArraySet<Integer> copyOnWriteArraySet = new CopyOnWriteArraySet<>();

		// 不支持null key, null value
		ConcurrentHashMap<Integer, String> concurrentHashMap = new ConcurrentHashMap<>(16);

		System.out.println();
	}
}
