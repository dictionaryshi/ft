package com.ft.br.study.juc;

/**
 * 单例
 *
 * @author shichunyang
 */
public class Singleton {

    /**
     * 这里的volatile关键字主要是为了防止指令重排, new Singleton() 分为三步, 1:分配内存空间; 2:初始化对象; 3:将instance变量指向分配的内存地址。
     * 第三步在第二步之前被执行就有可能导致某个线程拿到的单例对象还没有初始化。
     */
    private volatile static Singleton instance = null;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        System.out.println(Singleton.getInstance());
    }
}
