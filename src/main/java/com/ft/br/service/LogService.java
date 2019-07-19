package com.ft.br.service;

import com.ft.br.dao.LogMapper;
import com.ft.dao.stock.model.LogDO;
import com.ft.db.annotation.UseMaster;
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

	@UseMaster
	public Integer add(LogDO logDO) {
		logMapper.insertSelective(logDO);
		return logDO.getId();
	}
}
