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
		try {
			boolean f3 = blockingQueue.add(3);
		} catch (Exception e) {
			System.out.println();
		}

		// 返回队首元素, 但不删除队首元素。(队列为空时, 抛出异常)
		Integer firstElement = blockingQueue.element();

		// 队列空时, 调用remove方法抛出异常
		Integer n1 = blockingQueue.remove();
		Integer n2 = blockingQueue.remove();
		try {
			Integer n3 = blockingQueue.remove();
		} catch (Exception e) {
			System.out.println();
		}
		try {
			Integer first = blockingQueue.element();
		} catch (Exception e) {
			System.out.println();
		}

		blockingQueue = new ArrayBlockingQueue<>(2);
		boolean offer1 = blockingQueue.offer(1);
		boolean offer2 = blockingQueue.offer(2);
		// 队列满时返回false
		boolean offer3 = blockingQueue.offer(3);

		// 返回队首元素, 但不删除队首元素。(队列为空时, 返回null)
		Integer first = blockingQueue.peek();

		Integer n3 = blockingQueue.poll();
		Integer n4 = blockingQueue.poll();
		// 队列空时返回null
		Integer n5 = blockingQueue.poll();

		first = blockingQueue.peek();

		System.out.println();
	}
}
