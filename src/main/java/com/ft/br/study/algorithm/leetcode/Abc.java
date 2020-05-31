package com.ft.br.study.algorithm.leetcode;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ABC打印
 *
 * @author shichunyang
 */
public class Abc {
    private static Lock lock = new ReentrantLock();
    private static Condition aCondition = lock.newCondition();
    private static Condition bCondition = lock.newCondition();
    private static Condition cCondition = lock.newCondition();

    private static int count = 0;

    static class TaskA implements Runnable {
        private int number;

        TaskA(int number) {
            this.number = number;
        }

        @Override
        public void run() {
            lock.lock();
            try {
                for (int i = 0; i < number; i++) {
                    while (count % 3 != 0) {
                        // A线程等待
                        aCondition.await();
                    }
                    System.out.println(Thread.currentThread().getName());
                    count = 1;
                    // 唤醒B
                    bCondition.signal();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    static class TaskB implements Runnable {
        private int number;

        TaskB(int number) {
            this.number = number;
        }

        @Override
        public void run() {
            lock.lock();
            try {
                for (int i = 0; i < number; i++) {
                    while (count % 3 != 1) {
                        // B线程等待
                        bCondition.await();
                    }
                    System.out.println(Thread.currentThread().getName());
                    count = 2;
                    // 唤醒C
                    cCondition.signal();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    static class TaskC implements Runnable {
        private int number;

        TaskC(int number) {
            this.number = number;
        }

        @Override
        public void run() {
            lock.lock();
            try {
                for (int i = 0; i < number; i++) {
                    while (count % 3 != 2) {
                        // C线程等待
                        cCondition.await();
                    }
                    System.out.println(Thread.currentThread().getName());
                    System.out.println();
                    count = 0;
                    // 唤醒A
                    aCondition.signal();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        int number = 3;
        new Thread(new TaskA(number), "A").start();
        new Thread(new TaskB(number), "B").start();
        new Thread(new TaskC(number), "C").start();
    }
}
