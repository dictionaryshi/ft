package com.ft.dao;

import com.ft.redis.model.LogDO;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
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
	@Options(useCache = false, keyColumn = "id", useGeneratedKeys = true)
	@InsertProvider(type = LogSqlProvider.class, method = "insertSelective")
	int insertSelective(LogDO log);
}
