package com.ft.br.db;

import com.ft.db.dbutil.JdbcTemplateUtil;
import com.ft.util.JsonUtil;
import com.ft.web.model.UserDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JdbcTemplateTest {
	@Autowired
	@Qualifier("consignJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Test
	public void update() {
		String sql = "UPDATE `user` SET username = ? WHERE id = ?";
		int result = JdbcTemplateUtil.update(jdbcTemplate, sql, "zgl", 1);
		System.out.println(result);
	}

	@Test
	public void batchUpdate() {
		String sql = "INSERT INTO `user` (`username`, `password`) VALUES (?, ?)";
		List<Object[]> batchArgs = new ArrayList<>();
		batchArgs.add(new Object[]{"zgl", "963721"});
		batchArgs.add(new Object[]{"smy", "dianNao"});
		int[] result = JdbcTemplateUtil.batchUpdate(jdbcTemplate, sql, batchArgs);
		System.out.println(JsonUtil.object2Json(result));
	}

	@Test
	public void queryForObject() {
		String sql = "SELECT id, username, password, created_at createdAt, updated_at updatedAt FROM `user` WHERE id = ?";
		RowMapper<UserDO> rowMapper = new BeanPropertyRowMapper<>(UserDO.class);
		UserDO userDO = JdbcTemplateUtil.queryForObject(jdbcTemplate, sql, rowMapper, 1);
		System.out.println(JsonUtil.object2Json(userDO));
	}

	@Test
	public void query() {
		String sql = "SELECT id, username, password, created_at createdAt, updated_at updatedAt FROM `user` WHERE id > ?";
		RowMapper<UserDO> rowMapper = new BeanPropertyRowMapper<>(UserDO.class);
		List<UserDO> userList = JdbcTemplateUtil.query(jdbcTemplate, sql, rowMapper, 0);
		System.out.println(JsonUtil.object2Json(userList));
	}

	@Test
	public void queryForObjectCount() {
		String sql = "SELECT count(1) FROM `user` WHERE id > ?";
		Long count = JdbcTemplateUtil.queryForObject(jdbcTemplate, sql, Long.class, 0);
		System.out.println(count);
	}
}
