package com.ft.br.study.juc.check;

import com.ft.br.util.JsonUtil;
import com.ft.br.util.thread.ThreadPoolUtil;
import com.ft.util.JsonUtil;
import com.ft.util.thread.ThreadPoolUtil;
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
		String poolName = "check";
		ExecutorService executorService = ThreadPoolUtil.getThreadPool(poolName, 10, 20, 300, TimeUnit.SECONDS, 50, null);
		List<CheckCallable> callables = new ArrayList<CheckCallable>() {
			{
				add(new CheckCallable("用户信用校验") {
					@Override
					public Boolean call() {
						int length = 6_0000;
						for (int a = 0; a < length; a++) {
							for (int b = 0; b < length; b++) {
								for (int c = 0; c < length; c++) {
								}
							}
						}

						System.out.println("用户信用校验完毕");
						return Boolean.TRUE;
					}
				});

				add(new CheckCallable("用户身份校验") {
					@Override
					public Boolean call() {
						System.out.println("用户身份校验完毕");
						return Boolean.FALSE;
					}
				});

				add(new CheckCallable("银行校验") {
					@Override
					public Boolean call() {
						int length = 10_0000;
						for (int a = 0; a < length; a++) {
							for (int b = 0; b < length; b++) {
								for (int c = 0; c < length; c++) {
								}
							}
						}
						System.out.println("银行校验完毕");
						return Boolean.TRUE;
					}
				});
			}
		};

		boolean check = check(executorService, callables);
		log.info("final result==>{}", check);

		ThreadPoolUtil.shutdown(executorService, poolName);
	}
}
