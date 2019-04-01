package com.ft.br.dao;

import com.ft.redis.model.LogDO;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Component;

/**
 * LogMapper
 *
 * @author shichunyang
 */
@Component
@Mapper
public interface LogMapper {

	/**
	 * 插入日志
	 *
	 * @param log 日志
	 * @return 1:插入成功
	 */
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "id", keyProperty = "id", resultType = Integer.class, before = false)
	@InsertProvider(type = LogSqlProvider.class, method = "insertSelective")
	int insertSelective(LogDO log);
}
