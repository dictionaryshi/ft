package com.ft.br.study.juc;

import java.util.concurrent.atomic.AtomicLong;

/**
 * CAS(Compare and Swap):比较并替换。
 *
 * @author shichunyang
 */
public class CasTest {
	public static void main(String[] args) {
		AtomicLong atomicLong = new AtomicLong(100L);
		boolean flag1 = atomicLong.compareAndSet(100L, 500L);
		boolean flag2 = atomicLong.compareAndSet(300L, 700L);
		System.out.println();
	}
}
