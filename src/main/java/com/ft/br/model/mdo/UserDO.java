package com.ft.br.model.mdo;

import lombok.Data;

import java.util.Date;

/**
 * 用户
 *
 * @author shichunyang
 */
@Data
public class UserDO {
	private Long id;
	private String username;
	private String password;
	private Date createdAt;
	private Date updatedAt;

	public void init() {
		System.out.println(this.username + ", bean init");
	}

	public void destroy() {
		System.out.println(this.username + ", bean destroy");
	}
}
/*
    CREATE TABLE `user` (
      `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
      `username` varchar(50) NOT NULL DEFAULT '0' COMMENT '姓名',
      `password` varchar(50) NOT NULL DEFAULT '0' COMMENT '密码',
      `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */