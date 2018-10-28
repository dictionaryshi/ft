package com.ft.model.mdo;

import lombok.Data;

import java.io.Serializable;

/**
 * LogDO
 *
 * @author shichunyang
 */
@Data
public class LogDO implements Serializable {
	/**
	 * 主键(id)
	 */
	private Integer id;

	/**
	 * 时间戳(time)
	 */
	private Long time;

	/**
	 * 应用名称(application)
	 */
	private String application;

	/**
	 * 线程名称(thread)
	 */
	private String thread;

	/**
	 * 级别(level)
	 */
	private String level;

	/**
	 * 日志类名(logger)
	 */
	private String logger;

	/**
	 * 方法名称(method)
	 */
	private String method;

	/**
	 * 行号(line_number)
	 */
	private Integer lineNumber;

	/**
	 * 日志信息(message)
	 */
	private String message;

	/**
	 * 请求id
	 */
	private String requestId;

	/**
	 * 操作人
	 */
	private String userName;

	private static final long serialVersionUID = 1L;
}
/*
    CREATE TABLE `log` (
      `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
      `time` bigint(20) NOT NULL DEFAULT '0' COMMENT '时间戳',
      `application` varchar(50) NOT NULL DEFAULT '0' COMMENT '应用名称',
      `thread` varchar(255) NOT NULL DEFAULT '0' COMMENT '线程名称',
      `level` varchar(25) NOT NULL DEFAULT '0' COMMENT '级别',
      `logger` varchar(500) NOT NULL DEFAULT '0' COMMENT '日志类名',
      `method` varchar(255) NOT NULL DEFAULT '0' COMMENT '方法名称',
      `line_number` int(11) NOT NULL DEFAULT '0' COMMENT '行号',
      `message` longtext COMMENT '日志信息',
      `request_id` char(32) NOT NULL DEFAULT '0' COMMENT '请求id',
      `user_name` varchar(50) NOT NULL DEFAULT '0' COMMENT '操作人',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
