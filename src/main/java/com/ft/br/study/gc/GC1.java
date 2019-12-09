package com.ft.br.study.gc;

/**
 * GC1
 *
 * @author shichunyang
 */
public class GC1 {

	/**
	 * -Xms10m -Xmx10m -Xmn5m -Xss256k -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=10485760 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:/data/gc/gc_ft_%t-log
	 * 给堆内存分配10MB内存空间, 其中新生代是5MB内存空间, 其中Eden区占4MB, 每个Survivor区占0.5MB, 大对象必须超过10MB才会直接进入老年代。
	 * 测试时, 删除package, 使用javac 和 java 运行。
	 */
	public static void main(String[] args) {
		byte[] array1 = new byte[1024 * 1024];
		array1 = new byte[1024 * 1024];
		array1 = new byte[1024 * 1024];
		array1 = null;

		byte[] array2 = new byte[1024 * 1024 * 2];
	}
}
/*

0.094: [GC (Allocation Failure) 0.094: [ParNew: 3596K->301K(4608K), 0.0004948 secs] 3596K->301K(9728K), 0.0005602 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]

 */
