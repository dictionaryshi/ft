package com.ft.service;

import com.ft.dao.LogMapper;
import com.ft.db.annotation.DataSource;
import com.ft.model.mdo.LogDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 日志service
 *
 * @author shichunyang
 */
@Service
public class LogService {
	@Autowired
	private LogMapper logMapper;

	@DataSource
	public Integer add(LogDO logDO) {
		logMapper.insertSelective(logDO);
		return logDO.getId();
	}
}
