package com.ft.br.study.juc;

import com.ft.util.thread.ThreadPoolUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 控制对互斥资源的访问线程数
 *
 * @author shichunyang
 */
public class SemaphoreTest {
    public static void main(String[] args) throws Exception {
        int clientCount = 3;
        int totalRequestCount = 10;
        Semaphore semaphore = new Semaphore(clientCount);
        String poolName = "semaphore";
        ExecutorService executorService = ThreadPoolUtil.getThreadPool(poolName, 10, 20, 300, TimeUnit.SECONDS, 50, null);
        for (int i = 0; i < totalRequestCount; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + ", 抢到资源");
                    Thread.sleep(3_000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    semaphore.release();
                }
            });
        }
        ThreadPoolUtil.shutdown(executorService, poolName);
    }
}
