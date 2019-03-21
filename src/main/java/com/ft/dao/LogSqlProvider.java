package com.ft.dao;

import com.ft.redis.model.LogDO;
import org.apache.ibatis.jdbc.SQL;

/**
 * LogSqlProvider
 *
 * @author shichunyang
 */
public class LogSqlProvider {
	public String insertSelective(LogDO record) {
		SQL sql = new SQL();
		sql.INSERT_INTO("log");

		if (record.getTime() != null) {
			sql.VALUES("time", "#{time,jdbcType=BIGINT}");
		}

		if (record.getApplication() != null) {
			sql.VALUES("application", "#{application,jdbcType=VARCHAR}");
		}

		if (record.getThread() != null) {
			sql.VALUES("thread", "#{thread,jdbcType=VARCHAR}");
		}

		if (record.getLevel() != null) {
			sql.VALUES("level", "#{level,jdbcType=VARCHAR}");
		}

		if (record.getLogger() != null) {
			sql.VALUES("logger", "#{logger,jdbcType=VARCHAR}");
		}

		if (record.getMethod() != null) {
			sql.VALUES("method", "#{method,jdbcType=VARCHAR}");
		}

		if (record.getLineNumber() != null) {
			sql.VALUES("line_number", "#{lineNumber,jdbcType=INTEGER}");
		}

		if (record.getCost() != null) {
			sql.VALUES("cost", "#{cost,jdbcType=INTEGER}");
		}

		if (record.getRequestId() != null) {
			sql.VALUES("request_id", "#{requestId,jdbcType=CHAR}");
		}

		if (record.getMessage() != null) {
			sql.VALUES("message", "#{message,jdbcType=LONGVARCHAR}");
		}

		return sql.toString();
	}
}
