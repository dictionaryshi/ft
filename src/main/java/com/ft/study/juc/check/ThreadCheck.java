package com.ft.study.juc.check;

import com.ft.study.juc.ThreadPoolUtil;
import com.ft.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
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

			for (CheckFutureTask task : checkFutureTasks) {
				task.cancel(true);
			}

			for (CheckFutureTask task : checkFutureTasks) {
				if (!task.get()) {
					return false;
				}
			}
		} catch (Exception e) {
			if (e instanceof CancellationException) {
			} else {
				log.error("ThreadCheck exception==>{}", JsonUtil.object2Json(e), e);
			}
			return false;
		}

		return true;
	}

	public static void main(String[] args) throws Exception {
		ExecutorService executorService = ThreadPoolUtil.getThreadPool();
		List<CheckCallable> callables = new ArrayList<CheckCallable>() {
			{
				add(new CheckCallable("用户信用校验") {
					@Override
					public Boolean call() {
						try {
							Thread.sleep(10_000L);
							Thread.sleep(10_000L);
						} catch (InterruptedException e) {
						}
						return Boolean.TRUE;
					}
				});

				add(new CheckCallable("用户身份校验") {
					@Override
					public Boolean call() {
						try {
							Thread.sleep(5_000L);
						} catch (InterruptedException e) {
						}
						return Boolean.FALSE;
					}
				});

				add(new CheckCallable("银行校验") {
					@Override
					public Boolean call() {
						try {
							Thread.sleep(10_000L);
							Thread.sleep(10_000L);
							Thread.sleep(10_000L);
						} catch (InterruptedException e) {
						}
						return Boolean.TRUE;
					}
				});
			}
		};

		boolean check = check(executorService, callables);
		log.info("final result==>{}", check);

		executorService.shutdown();

		while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
			System.out.println("线程池中仍然有线程执行");
		}
	}
}
