package com.ft.br.redis;

import com.ft.redis.base.ValueOperationsCache;
import com.ft.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {
	@Autowired
	private ValueOperationsCache<String, String> valueOperationsCache;

	private static final String LOGIN_ID = "login_id";
	private static final String USER_NAME = "user_name";

	/**
	 * 删除key
	 */
	@Test
	public void delete() {
		valueOperationsCache.delete(Arrays.asList(LOGIN_ID, USER_NAME));
	}

	/**
	 * 判断key是否存在
	 */
	@Test
	public void hasKey() {
		boolean isHas = valueOperationsCache.hasKey(LOGIN_ID);
		System.out.println(isHas);
	}

	/**
	 * 设置key的有效期(毫秒)
	 */
	@Test
	public void expire() {
		boolean flag = valueOperationsCache.expire(LOGIN_ID, 30_000L, TimeUnit.MILLISECONDS);
		System.out.println(flag);
	}

	/**
	 * 设置key的指定过期时间
	 */
	@Test
	public void expireAt() {
		boolean flag = valueOperationsCache.expireAt(LOGIN_ID, new Date(System.currentTimeMillis() + 50_000L));
		System.out.println(flag);
	}

	/**
	 * 获取key的生存周期(-2:已经被删除,-1:永久存活,其它:单位是毫秒)
	 */
	@Test
	public void getExpire() {
		long milliSeconds = valueOperationsCache.getExpire(LOGIN_ID, TimeUnit.MILLISECONDS);
		System.out.println(milliSeconds);
	}

	/**
	 * 查询所有key的集合
	 */
	@Test
	public void keys() {
		Set<String> keys = valueOperationsCache.keys("*");
		System.out.println(JsonUtil.object2Json(keys));
	}

	/**
	 * 将key移动到指定库中(3号库)
	 */
	@Test
	public void move() {
		boolean flag = valueOperationsCache.move(LOGIN_ID, 3);
		System.out.println(flag);
	}

	/**
	 * 重命名key
	 */
	@Test
	public void rename() {
		valueOperationsCache.rename(LOGIN_ID, LOGIN_ID + "_new");
	}

	/**
	 * 查看key的类型
	 */
	@Test
	public void type() {
		DataType dataType = valueOperationsCache.type(LOGIN_ID);
		System.out.println(dataType.code());
	}

	/**
	 * 查看字符串长度(中文字符串不准确)
	 */
	@Test
	public void strSize() {
		long length = valueOperationsCache.size(LOGIN_ID);
		System.out.println(length);
	}

	/**
	 * 追加字符串, 并返回追加后字符串的长度(对中文返回的长度不准确)
	 */
	@Test
	public void append() {
		for (int i = 1; i <= 5; i++) {
			long length = valueOperationsCache.append(LOGIN_ID, i + ",");
			System.out.println(length);
		}
	}

	/**
	 * 自增, 并返回key值
	 */
	@Test
	public void increment() {
		for (int i = 1; i <= 5; i++) {
			long result = valueOperationsCache.increment(LOGIN_ID, 1);
			System.out.println(result);
		}
	}

	/**
	 * 自增, 并在第一次设置过期时间
	 */
	@Test
	public void incrementWithExpire() {
		for (int i = 1; i <= 5; i++) {
			long result = valueOperationsCache.increment(LOGIN_ID, 1, 40_000L);
			System.out.println(result);
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * 截取字符串(留头也留尾)
	 */
	@Test
	public void subString() {
		String result = valueOperationsCache.get(LOGIN_ID, 0, 10);
		System.out.println(result);
	}

	/**
	 * 根据key获取值
	 */
	@Test
	public void strGet() {
		String value = valueOperationsCache.get(LOGIN_ID);
		System.out.println(value);
	}

	/**
	 * 设置不过期的键值
	 */
	@Test
	public void strSet() {
		valueOperationsCache.set(LOGIN_ID, "13264232894");
	}

	/**
	 * 设置带过期时间(毫秒)的key value
	 */
	@Test
	public void setEX() {
		valueOperationsCache.set(LOGIN_ID, "13264232894", 60_000L, TimeUnit.MILLISECONDS);
	}

	/**
	 * 设置key value 如果key存在 不会覆盖
	 */
	@Test
	public void setNX() {
		boolean flag = valueOperationsCache.setIfAbsent(LOGIN_ID, "13264232894");
		System.out.println(flag);
	}

	/**
	 * 设置带过期时间的key value 如果key存在,不会覆盖
	 */
	@Test
	public void setNXWithExpire() {
		boolean flag = valueOperationsCache.setIfAbsent(LOGIN_ID, "13264232894", 60_000L, TimeUnit.MILLISECONDS);
		System.out.println(flag);
	}
}
