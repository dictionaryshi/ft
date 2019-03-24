package com.ft.br.redis;

import com.ft.redis.base.ListOperationsCache;
import com.ft.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * ListOperationsCacheTest
 *
 * @author shichunyang
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ListOperationsCacheTest {

	@Resource(name = "listOperationsCache")
	private ListOperationsCache listOperationsCache;

	public static final String NUMBER_LIST = "number_list";

	/**
	 * 从头部依次插入, 并返回List长度
	 */
	@Test
	public void leftPushAll() {
		long length = listOperationsCache.leftPushAll(NUMBER_LIST, "1", "2", "3");
		System.out.println(length);
	}

	/**
	 * 从左到右遍历List
	 */
	@Test
	public void range() {
		List<String> numberList = listOperationsCache.range(NUMBER_LIST, 0, -1);
		System.out.println(JsonUtil.object2Json(numberList));
	}

	/**
	 * 从尾部依次插入, 返回List长度
	 */
	@Test
	public void rightPushAll() {
		long length = listOperationsCache.rightPushAll(NUMBER_LIST, "1", "2", "3");
		System.out.println(length);
	}

	/**
	 * 移除并返回头元素
	 */
	@Test
	public void leftPop() {
		String value = listOperationsCache.leftPop(NUMBER_LIST);
		System.out.println(value);
	}

	/**
	 * 移除并返回尾元素
	 */
	@Test
	public void rightPop() {
		String value = listOperationsCache.rightPop(NUMBER_LIST);
		System.out.println(value);
	}

	/**
	 * 返回list长度
	 */
	@Test
	public void size() {
		long length = listOperationsCache.size(NUMBER_LIST);
		System.out.println(length);
	}

	/**
	 * 从头部开始删除, 返回删除的个数
	 */
	@Test
	public void remove() {
		long size = listOperationsCache.remove(NUMBER_LIST, 0, "2");
		System.out.println(size);
	}

	/**
	 * 截取List并给List赋值(留头也留尾)
	 */
	@Test
	public void trim() {
		listOperationsCache.trim(NUMBER_LIST, 0, 2);
	}
}
