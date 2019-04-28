package com.ft.br.study.juc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 阻塞队列:队列空时从队列获取元素会被阻塞, 队列满时往队列添加元素会被阻塞。
 */
public class QueueTest {
	public static void main(String[] args) {
		BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(2);
		// 队列满时, 调用add方法抛出异常
		boolean f1 = blockingQueue.add(1);
		boolean f2 = blockingQueue.add(2);

		// 返回队首元素, 但不删除队首元素。(队列为空时, 抛出异常)
		Integer element = blockingQueue.element();

		// 队列空时, 调用remove方法抛出异常
		Integer n1 = blockingQueue.remove();
		Integer n2 = blockingQueue.remove();

		System.out.println();
	}
}
