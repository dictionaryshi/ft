package com.ft.br.study.juc.check;

import com.ft.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;

/**
 * CheckFutureTask
 *
 * @author shichunyang
 */
@Slf4j
public class CheckFutureTask extends FutureTask<Boolean> {
	private volatile CountDownLatch countDownLatch;

	private final int taskNumber;

	private final String currentTaskName;

	public CheckFutureTask(CheckCallable callable, CountDownLatch countDownLatch, int taskNumber, String currentTaskName) {
		super(callable);
		this.countDownLatch = countDownLatch;
		this.taskNumber = taskNumber;
		this.currentTaskName = currentTaskName;
	}

	@Override
	protected void done() {
		try {
			if (!super.get()) {
				afterFail();
				log.warn("currentTaskName==>{}, 未通过", currentTaskName);
			} else {
				log.info("currentTaskName==>{}, 已通过", currentTaskName);
			}
		} catch (Exception e) {
			this.afterFail();
			if (e instanceof CancellationException) {
				log.warn("taskName==>{}, 被取消", currentTaskName);
			} else {
				log.error("currentTaskName==>{}, exception==>{}", currentTaskName, JsonUtil.object2Json(e), e);
			}
		} finally {
			countDownLatch.countDown();
		}
	}

	private void afterFail() {
		for (int i = 0; i < taskNumber - 1; i++) {
			countDownLatch.countDown();
		}
	}
}
