package com.ft.config.db;

import com.ft.db.util.MybatisUtil;
import com.ft.constant.DbConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * ConsignMyBatisConfig
 *
 * @author shichunyang
 */
@Slf4j
@Configuration
@MapperScan(basePackages = {"com.ft.dao"}, sqlSessionFactoryRef = DbConstant.DB_CONSIGN + "SqlSessionFactory")
public class ConsignMyBatisConfig {

	private final DataSource consignDataSource;

	@Autowired
	public ConsignMyBatisConfig(@Qualifier(DbConstant.DB_CONSIGN + "DataSource") DataSource consignDataSource) {
		this.consignDataSource = consignDataSource;
	}

	@Bean(DbConstant.DB_CONSIGN + "SqlSessionFactory")
	public SqlSessionFactory consignSqlSessionFactory() {
		try {
			return MybatisUtil.sqlSessionFactory(consignDataSource);
		} catch (Exception e) {
			log.error(DbConstant.DB_CONSIGN + "SqlSessionFactory fail", e);
			throw new RuntimeException(DbConstant.DB_CONSIGN + "SqlSessionFactory fail");
		}
	}

	@Bean(DbConstant.DB_CONSIGN + "SqlSessionTemplate")
	public SqlSessionTemplate consignSqlSessionTemplate(@Qualifier(DbConstant.DB_CONSIGN + "SqlSessionFactory") SqlSessionFactory consignSqlSessionFactory) {
		return new SqlSessionTemplate(consignSqlSessionFactory);
	}
}
