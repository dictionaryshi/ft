package com.ft.br.study.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class AtomicReferenceLock {
    private AtomicReference<Thread> threadAtomicReference = new AtomicReference<>();

    public void lock() {
        while (true) {
            if (threadAtomicReference.compareAndSet(null, Thread.currentThread())) {
                break;
            }
        }
        log.info("thread==>{}, 获取锁成功", Thread.currentThread().getName());
    }

    public void unlock() {
        boolean flag = threadAtomicReference.compareAndSet(Thread.currentThread(), null);
        log.info("thread==>{}, 释放锁结果==>{}", Thread.currentThread().getName(), flag);
    }

    public static void main(String[] args) {
        AtomicReferenceLock lock = new AtomicReferenceLock();
        lock.lock();
        lock.unlock();
        lock.lock();
    }
}
