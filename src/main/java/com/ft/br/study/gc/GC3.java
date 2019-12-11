package com.ft.br.study.gc;

/**
 * GC3
 *
 * @author shichunyang
 */
public class GC3 {

	/**
	 * -Xms20m -Xmx20m -Xmn10m -Xss256k -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=15 -XX:PretenureSizeThreshold=3145728 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:/data/gc/gc_ft_%t-log
	 * 给堆内存分配20MB内存空间, 其中新生代是10MB内存空间, 其中Eden区占8MB, 每个Survivor区占1MB, 大对象必须超过3MB才会直接进入老年代。
	 */
	public static void main(String[] args) {
		// 大对象直接在老年代分配
		byte[] array1 = new byte[4 * 1024 * 1024];
		array1 = null;

		byte[] array2 = new byte[2 * 1024 * 1024];
		byte[] array3 = new byte[2 * 1024 * 1024];
		byte[] array4 = new byte[2 * 1024 * 1024];
		byte[] array5 = new byte[500 * 1024];

		// array6在Eden创建前, 发生一次YGC。
		byte[] array6 = new byte[2 * 1024 * 1024];
	}
}
