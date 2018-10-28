package com.ft.basic;

import com.ft.db.dbutil.TxDataSourcePool;
import com.ft.model.mdo.UserDO;
import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.net.URL;
import java.sql.Connection;
import java.util.Arrays;

public class BasicTest {
	@Test
	public void char2Number() {
		int result = 'a';
		System.out.println(result);

		System.out.println(2 << 3);
	}

	@Test
	public void extendsTest() {
		new Son();
	}

	@Test
	public void InnerClass() {
		Body body = new Body();
		body.bodyFunction();

		Body.InnerClass.show2();
	}

	@Test
	public void charTest() {
		char[] chs = new char[2];
		chs[1] = 'a';
		System.out.println('\u0000' == chs[0]);
	}

	@Test
	public void copyValueOf() {
		String str = "abcdefg";
		str = String.copyValueOf(str.toCharArray(), 2, 3);
		System.out.println(str);
	}

	@Test
	public void exit() {
		System.exit(0);
	}

	@Test
	public void url() throws Exception {
		String requestUrl = "http://10.0.10.40:38080/user/userServlet?k1=k2&k2=v2";

		URL url = new URL(requestUrl);

		System.out.println(url.getProtocol());
		System.out.println(url.getHost());
		System.out.println(url.getPort());
		System.out.println(url.getFile());
		System.out.println(url.getPath());
		System.out.println(url.getQuery());
	}

	@Test
	public void cache() {
		Runtime runtime = Runtime.getRuntime();
		com.sun.management.OperatingSystemMXBean osmb = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

		// CPU核数
		System.out.println(osmb.getAvailableProcessors());
		System.out.println(runtime.availableProcessors());

		// 系统版本
		System.out.println(osmb.getName());

		long totalPhysicalMemorySize = osmb.getTotalPhysicalMemorySize();
		long totalCache = totalPhysicalMemorySize / 1024 / 1024;
		// 内存 总量
		System.out.println(totalCache);

		long freeCache = osmb.getFreePhysicalMemorySize() / 1024 / 1024;
		// 可用内存
		System.out.println(freeCache);
	}

	@Test
	public void sortStr() {
		String[] arr = {"d", "a", "c", "g", "e", "b", "f"};
		Arrays.sort(arr, String.CASE_INSENSITIVE_ORDER);
		System.out.println(Arrays.toString(arr));
	}

	@Test
	public void base() {
		new A();
	}

	@Test
	public void connection() throws Exception {
		Connection conn = new TxDataSourcePool().getConnection();
		// 读未提交
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		// 读已提交
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		// 可重复读
		conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
		// 串行化
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
	}
}

class A extends Base<UserDO> {
}

class Grandfather {
	static {
		System.out.println("Grandfather静态代码块");
	}

	{
		System.out.println("Grandfather构造代码块");
	}

	public Grandfather() {
		super();
		System.out.println("Grandfather默认构造方法");
	}
}

class Father extends Grandfather {

	static {
		System.out.println("Father静态代码块");
	}

	{
		System.out.println("Father构造代码块");
	}

	public Father() {
		super();
		System.out.println("Father默认构造方法");
	}
}

class Son extends Father {
	static {
		System.out.println("Son静态代码块");
	}

	{
		System.out.println("Son构造代码块");
	}

	public Son() {
		super();
		System.out.println("Son默认构造方法");
	}
}
