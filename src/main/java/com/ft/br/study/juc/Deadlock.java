package com.ft.br.study.juc;

import java.util.concurrent.TimeUnit;

public class Deadlock implements Runnable {
    private final String lockA;
    private final String lockB;

    public Deadlock(String lockA, String lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }

    @Override
    public void run() {
        synchronized (lockA) {
            System.out.println(Thread.currentThread().getName() + ", 持有锁==>" + lockA + ", 准备尝试获得锁==>" + lockB);
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            synchronized (lockB) {
                System.out.println(Thread.currentThread().getName() + ", 正常结束");
            }
        }
    }

    public static void deadLock() {
        String lockA = "A";
        String lockB = "B";
        new Thread(new Deadlock(lockA, lockB), "One").start();
        new Thread(new Deadlock(lockB, lockA), "Two").start();
    }

    public static void main(String[] args) {
        deadLock();
    }
}
