package com.ft.br.dao;

import com.ft.dao.stock.model.LogDO;
import com.ft.db.constant.DbConstant;
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
	@SelectKey(statement = DbConstant.SELECT_LAST_INSERT_ID, keyColumn = "id", keyProperty = "id", resultType = Integer.class, before = false)
	@InsertProvider(type = LogSqlProvider.class, method = "insertSelective")
	int insertSelective(LogDO log);
}
