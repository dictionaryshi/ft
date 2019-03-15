package com.ft.db;

import com.ft.db.dbutil.TxQueryRunner;
import com.ft.util.JsonUtil;
import com.ft.util.ObjectUtil;
import com.ft.model.mdo.UserDO;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DBUtils Test
 *
 * @author shichunyang
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class QueryRunnerTest {

	@Autowired
	private TxQueryRunner txQueryRunner;

	/**
	 * 测试批量添加操作
	 */
	@Test
	public void txBatch() throws Exception {

		String sql = "INSERT INTO `user` (`username`, `password`) VALUES (?, ?) ";

		Object[][] params = new Object[20][];//插入n行记录

		for (int i = 0; i < params.length; i++) {
			params[i] = new Object[]{"zgl" + i, i + ""};
		}

		int[] result = txQueryRunner.txBatch(sql, params);
		System.out.println(Arrays.toString(result));
	}

	/**
	 * 测试删除
	 */
	@Test
	public void txUpdate() throws Exception {
		long id = 14;
		String sql = "delete from `user` where id = ?";
		int result = txQueryRunner.txUpdate(sql, id);
		Assert.assertTrue(result == 1);
	}

	@Test
	public void txQueryById() throws Exception {

		String sql = " SELECT * FROM `user` t WHERE t.id = ? ";

		UserDO user = txQueryRunner.txQuery(sql, new BeanHandler<>(UserDO.class), 5);

		System.out.println(JsonUtil.object2Json(user));
	}

	@Test
	public void txQueryByIdToMap() throws Exception {
		String sql = " SELECT " +
				"`id`, " +
				"`username`, " +
				"`password`, " +
				"`created_at` createdAt, " +
				"`updated_at` updatedAt " +
				"FROM `user` t WHERE t.id = ? ";

		// 单行处理器,将ResultSet对象转换为Map<String,Object>其中列名为map的键
		Map<String, Object> map = txQueryRunner.txQuery(sql, new MapHandler(), 5);

		for (Map.Entry<String, Object> kv : map.entrySet()) {
			System.out.print(kv.getKey() + "==>" + kv.getValue());
			System.out.println();
		}

		UserDO user = ObjectUtil.map2Bean(map, UserDO.class, null);
		System.out.println(JsonUtil.object2Json(user));

		Map<String, Object> userMap = new HashMap<>();

		userMap.put("id", 666);
		userMap.put("username", "史春阳");
		userMap.put("password", "naodian12300");
		userMap.put("createdAt", "2017-11-11 11:11:11");
		userMap.put("updatedAt", "2018-12-12 12:12:12");

		user = ObjectUtil.map2Bean(userMap, UserDO.class, "yyyy-MM-dd HH:mm:ss");

		System.out.println(JsonUtil.object2Json(user));
	}

	@Test
	public void txQueryList() throws Exception {

		String sql = "SELECT * FROM `user` where `id` > 10";

		Object[] params = {};

		// 多行处理器,将ResultSet对象转换成List<JavaBean>,同样需要JavaBean的Class作为参数
		List<UserDO> userList = txQueryRunner.txQuery(sql, new BeanListHandler<>(UserDO.class), params);
		System.out.println(JsonUtil.object2Json(userList));
	}

	@Test
	public void txQueryListToMap() throws Exception {

		String sql = "SELECT * FROM `user` where `id` > 10";
		Object[] params = {};

		// 多行处理器,将ResultSet对象转换为List<Map<String, Object>>
		List<Map<String, Object>> list = txQueryRunner.txQuery(sql, new MapListHandler(), params);

		for (Map<String, Object> map : list) {

			for (Map.Entry<String, Object> kv : map.entrySet()) {
				System.out.print(kv.getKey() + "==>" + kv.getValue() + ",");
			}

			System.out.println();
		}
	}

	@Test
	public void txQuerySingleColumn() throws Exception {

		String sql = "SELECT `username` FROM `user` where `id` > 10";

		Object[] params = {};
		// 多行单列处理器, 将ResultSet对象转换为List<Object>, 需要用列名作为参数。
		List<String> usernameList = txQueryRunner.txQuery(sql, new ColumnListHandler<>("username"), params);
		System.out.println(JsonUtil.object2Json(usernameList));
	}

	@Test
	public void txQueryCount() throws Exception {

		String sql = "SELECT COUNT(1) FROM `user` ";

		Object[] params = {};

		// 单行单列处理器, 将ResultSet对象转换为Object(使用聚合函数查询)
		Object obj = txQueryRunner.txQuery(sql, new ScalarHandler<>(), params);

		Number num = (Number) obj;

		long count = num.longValue();
		System.out.println(count);
	}
}
