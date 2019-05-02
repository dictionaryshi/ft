package com.ft.br.study.juc;

import java.util.WeakHashMap;

/**
 * 1.强引用对象:即使出现OOM也不会被回收。
 * 2.软引用对象:OOM发生前可以被回收。
 * 3.弱引用对象:只要被GC扫描到就会回收。
 */
public class ReferenceTest {
	private static void weakHashMapTest() {
		WeakHashMap<Integer, String> weakHashMap = new WeakHashMap<>();
		Integer key = 256;
		weakHashMap.put(key, "weakHashMap");

		key = null;

		System.out.println(weakHashMap);
		System.gc();
		System.out.println(weakHashMap);
	}

	public static void main(String[] args) {
		weakHashMapTest();
	}
}
