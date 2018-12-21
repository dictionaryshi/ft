package com.ft.study.juc.check;

import lombok.Getter;

import java.util.concurrent.Callable;

/**
 * CheckCallable
 *
 * @author shichunyang
 */
@Getter
public class CheckCallable implements Callable<Boolean> {
	private final String log;

	public CheckCallable(String log) {
		this.log = log;
	}

	@Override
	public Boolean call() {
		return null;
	}
}
