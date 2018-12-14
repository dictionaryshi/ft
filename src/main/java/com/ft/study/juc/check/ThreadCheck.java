package com.ft.study.juc.check;

import com.ft.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

/**
 * ThreadCheck
 *
 * @author shichunyang
 */
@Slf4j
public class ThreadCheck {
	public static boolean check(
			ExecutorService executorService,
			List<CheckCallable> callables
	) {
		int taskNumber = callables.size();
		CountDownLatch countDownLatch = new CountDownLatch(taskNumber);
		List<CheckFutureTask> checkFutureTasks = callables.stream().map(callable -> new CheckFutureTask(callable, countDownLatch, taskNumber, callable.getLog())).collect(Collectors.toList());
		for (FutureTask<Boolean> task : checkFutureTasks) {
			executorService.execute(task);
		}
		try {
			countDownLatch.await();

			for (FutureTask<Boolean> task : checkFutureTasks) {
				task.cancel(true);
			}

			for (FutureTask<Boolean> task : checkFutureTasks) {
				if (!task.get()) {
					return false;
				}
			}
		} catch (Exception e) {
			log.error("ThreadCheck exception==>{}", JsonUtil.object2Json(e), e);
			return false;
		}

		return true;
	}
}
