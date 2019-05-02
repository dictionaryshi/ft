package com.ft.br.study.juc;

import com.ft.util.exception.FtException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Error {
	/**
	 * java.lang.StackOverflowError
	 */
	private static void stackOverflowError() {
		stackOverflowError();
	}

	/**
	 * java.lang.OutOfMemoryError: Java heap space(堆内存不足)
	 */
	private static void javaHeapSpace() {
		byte[] bytes = new byte[20 * 1024 * 1024];
	}

	/**
	 * java.lang.OutOfMemoryError: GC overhead limit exceeded(超过98%的时间用来做GC并且回收了不到2%的堆内存时会抛出此异常)
	 */
	private static void gCOverheadLimitExceeded() {
		List<String> cacheList = new ArrayList<>();
		int i = 0;
		while (true) {
			cacheList.add(++i + "");
		}
	}

	/**
	 * java.lang.OutOfMemoryError: unable to create new native thread(单个应用创建过多线程抛出此异常)
	 */
	private static void unableToCreateNewNativeThread() {
		while (true) {
			new Thread(() -> {
				try {
					Thread.sleep(30_000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
		}
	}

	public static void main(String[] args) {
		try {
			stackOverflowError();
		} catch (Throwable throwable) {
			log.error(FtException.getExceptionStack(throwable));
		}

		try {
			javaHeapSpace();
		} catch (Throwable throwable) {
			log.error(FtException.getExceptionStack(throwable));
		}

		try {
			gCOverheadLimitExceeded();
		} catch (Throwable throwable) {
			log.error(FtException.getExceptionStack(throwable));
		}

		try {
			unableToCreateNewNativeThread();
		} catch (Throwable throwable) {
			log.error(FtException.getExceptionStack(throwable));
		}
	}
}
