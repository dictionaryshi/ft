package com.ft.basic;

import com.ft.db.dbutil.TxDataSourcePool;
import com.ft.study.juc.ThreadPoolUtil;
import org.junit.Test;

import java.net.URL;
import java.sql.Connection;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class BasicTest {
	@Test
	public void char2Number() {
		int result = 'a';
		System.out.println(result);

		System.out.println(2 << 3);
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
	public void sortStr() {
		String[] arr = {"d", "a", "c", "g", "e", "b", "f"};
		Arrays.sort(arr, String.CASE_INSENSITIVE_ORDER);
		System.out.println(Arrays.toString(arr));
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

	@Test
	public void getThreadPool() throws Exception {
		ExecutorService threadPool = ThreadPoolUtil.getThreadPool();
		int number = 6;
		for (int i = 1; i <= number; i++) {
			int finalNumber = i;
			threadPool.execute(() -> {
				try {
					Thread.sleep(5_000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(finalNumber);
			});
		}
		threadPool.shutdown();

		while (!threadPool.awaitTermination(1, TimeUnit.SECONDS)) {
			System.out.println("线程池中仍然有线程执行");
		}
	}
}
