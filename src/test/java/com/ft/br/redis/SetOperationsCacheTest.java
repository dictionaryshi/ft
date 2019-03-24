package com.ft.br.redis;

import com.ft.redis.base.SetOperationsCache;
import com.ft.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Set;

/**
 * SetOperationsCacheTest
 *
 * @author shichunyang
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SetOperationsCacheTest {

	@Resource(name = "setOperationsCache")
	private SetOperationsCache setOperationsCache;

	private static final String NUMBER_SET = "number_set";

	/**
	 * 添加元素
	 */
	@Test
	public void add() {
		long length = setOperationsCache.add(NUMBER_SET, "1", "2", "3");
		System.out.println(length);
	}

	/**
	 * 获取set
	 */
	@Test
	public void members() {
		Set<String> set = setOperationsCache.members(NUMBER_SET);
		System.out.println(JsonUtil.object2Json(set));
	}

	/**
	 * 判断指定值是否存在
	 */
	@Test
	public void isMember() {
		boolean flag = setOperationsCache.isMember(NUMBER_SET, "2");
		System.out.println(flag);
	}

	/**
	 * 获取set大小
	 */
	@Test
	public void size() {
		long length = setOperationsCache.size(NUMBER_SET);
		System.out.println(length);
	}

	/**
	 * 删除值
	 */
	@Test
	public void remove() {
		long size = setOperationsCache.remove(NUMBER_SET, "1", "2", "3");
		System.out.println(size);
	}

	/**
	 * 随机删除并返回一个值
	 */
	@Test
	public void pop() {
		String value = setOperationsCache.pop(NUMBER_SET);
		System.out.println(value);
	}
}
