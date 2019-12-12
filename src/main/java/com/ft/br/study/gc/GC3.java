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
/*
CommandLine flags: -XX:InitialHeapSize=20971520 -XX:MaxHeapSize=20971520 -XX:MaxNewSize=10485760 -XX:MaxTenuringThreshold=15 -XX:NewSize=10485760 -XX:OldPLABSize=16 -XX:PretenureSizeThreshold=3145728 -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:SurvivorRatio=8 -XX:ThreadStackSize=256 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseConcMarkSweepGC -XX:+UseParNewGC
0.083: [GC (Allocation Failure) 0.083: [ParNew (promotion failed): 7152K->7953K(9216K), 0.0123063 secs]0.095: [CMS: 8194K->6925K(10240K), 0.0024515 secs] 11248K->6925K(19456K), [Metaspace: 2621K->2621K(1056768K)], 0.0148549 secs] [Times: user=0.04 sys=0.00, real=0.01 secs]
Heap
 par new generation   total 9216K, used 2212K [0x00000007bec00000, 0x00000007bf600000, 0x00000007bf600000)
  eden space 8192K,  27% used [0x00000007bec00000, 0x00000007bee290e0, 0x00000007bf400000)
  from space 1024K,   0% used [0x00000007bf500000, 0x00000007bf500000, 0x00000007bf600000)
  to   space 1024K,   0% used [0x00000007bf400000, 0x00000007bf400000, 0x00000007bf500000)
 concurrent mark-sweep generation total 10240K, used 6925K [0x00000007bf600000, 0x00000007c0000000, 0x00000007c0000000)
 Metaspace       used 2628K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 286K, capacity 386K, committed 512K, reserved 1048576K
 */
