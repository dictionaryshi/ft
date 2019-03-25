package com.ft.br.config.db;

import com.ft.db.constant.DbConstant;
import com.ft.db.util.MybatisUtil;
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
@MapperScan(basePackages = {"com.ft.br.dao"}, sqlSessionFactoryRef = DbConstant.DB_CONSIGN + DbConstant.SQL_SESSION_FACTORY)
public class ConsignMyBatisConfig {

	@Autowired
	@Qualifier(DbConstant.DB_CONSIGN + DbConstant.DATA_SOURCE)
	private DataSource dataSource;

	@Bean(DbConstant.DB_CONSIGN + DbConstant.SQL_SESSION_FACTORY)
	public SqlSessionFactory sqlSessionFactory() {
		try {
			return MybatisUtil.sqlSessionFactory(dataSource);
		} catch (Exception e) {
			log.error(DbConstant.DB_CONSIGN + DbConstant.SQL_SESSION_FACTORY_FAIL, e);
			throw new RuntimeException(DbConstant.DB_CONSIGN + DbConstant.SQL_SESSION_FACTORY_FAIL);
		}
	}

	@Bean(DbConstant.DB_CONSIGN + DbConstant.SQL_SESSION_TEMPLATE)
	public SqlSessionTemplate sqlSessionTemplate(@Qualifier(DbConstant.DB_CONSIGN + DbConstant.SQL_SESSION_FACTORY) SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
