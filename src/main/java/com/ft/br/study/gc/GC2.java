package com.ft.br.study.gc;

/**
 * GC2
 *
 * @author shichunyang
 */
public class GC2 {

	/**
	 * -Xms20m -Xmx20m -Xmn10m -Xss256k -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=10485760 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:/data/gc/gc_ft_%t-log
	 * 给堆内存分配20MB内存空间, 其中新生代是10MB内存空间, 其中Eden区占8MB, 每个Survivor区占1MB, 大对象必须超过10MB才会直接进入老年代。
	 */
	public static void main(String[] args) {
		byte[] array1 = new byte[2 * 1024 * 1024];
		array1 = new byte[2 * 1024 * 1024];
		array1 = new byte[2 * 1024 * 1024];
		array1 = null;

		byte[] array2 = new byte[128 * 1024];

		byte[] array3 = new byte[2 * 1024 * 1024];

		array3 = new byte[2 * 1024 * 1024];
		array3 = new byte[2 * 1024 * 1024];
		array3 = new byte[128 * 1024];
		array3 = null;

		byte[] array4 = new byte[2 * 1024 * 1024];
	}
}
