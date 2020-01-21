package com.ft.br.study.algorithm.leetcode;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * 日志限速器
 *
 * @author shichunyang
 */
public class LoggerLeetCode {

	private class Pair<U, V> {
		private U first;
		private V second;

		private Pair(U first, V second) {
			this.first = first;
			this.second = second;
		}
	}

	private LinkedList<Pair<String, Integer>> msgQueue;
	private HashSet<String> msgSet;

	public LoggerLeetCode() {
		msgQueue = new LinkedList<>();
		msgSet = new HashSet<>();
	}

	public boolean shouldPrintMessage(int timestamp, String message) {
		while (msgQueue.size() > 0) {
			Pair<String, Integer> head = msgQueue.getFirst();
			if (timestamp - head.second >= 10) {
				msgQueue.removeFirst();
				msgSet.remove(head.first);
			} else {
				break;
			}
		}

		if (!msgSet.contains(message)) {
			Pair<String, Integer> newEntry = new Pair<>(message, timestamp);
			msgQueue.addLast(newEntry);
			msgSet.add(message);
			return true;
		} else {
			return false;
		}
	}
}
