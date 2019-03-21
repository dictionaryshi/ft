package com.ft.config.db;

import com.ft.db.constant.DbConstant;
import com.ft.db.model.DruidDataSourceDO;
import com.ft.db.model.DynamicDataSource;
import com.ft.db.plugin.FtDataSourceTransactionManager;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * 常量数据库配置
 *
 * @author shichunyang
 */
@Configuration
public class ConsignDataSourceConfig {

	@Bean(DbConstant.DB_CONSIGN + DbConstant.DATA_SOURCE_MASTER)
	@ConfigurationProperties(prefix = DbConstant.DRUID + DbConstant.DB_CONSIGN + DbConstant.MASTER)
	public DruidDataSourceDO dataSourceMaster() {
		return new DruidDataSourceDO();
	}

	@Bean(DbConstant.DB_CONSIGN + DbConstant.DATA_SOURCE_SLAVE1)
	@ConfigurationProperties(prefix = DbConstant.DRUID + DbConstant.DB_CONSIGN + DbConstant.SLAVE1)
	public DruidDataSourceDO dataSourceSlave1() {
		return new DruidDataSourceDO();
	}

	@Bean(DbConstant.DB_CONSIGN + DbConstant.DATA_SOURCE_SLAVE2)
	@ConfigurationProperties(prefix = DbConstant.DRUID + DbConstant.DB_CONSIGN + DbConstant.SLAVE2)
	public DruidDataSourceDO dataSourceSlave2() {
		return new DruidDataSourceDO();
	}

	@Bean(DbConstant.DB_CONSIGN + DbConstant.DATA_SOURCE)
	public DataSource dataSource(
			@Qualifier(DbConstant.DB_CONSIGN + DbConstant.DATA_SOURCE_MASTER) DruidDataSourceDO dataSourceMaster,
			@Qualifier(DbConstant.DB_CONSIGN + DbConstant.DATA_SOURCE_SLAVE1) DruidDataSourceDO dataSourceSlave1,
			@Qualifier(DbConstant.DB_CONSIGN + DbConstant.DATA_SOURCE_SLAVE2) DruidDataSourceDO dataSourceSlave2
	) {
		return DynamicDataSource.instance(dataSourceMaster, dataSourceSlave1, dataSourceSlave2);
	}

	@Bean(DbConstant.DB_CONSIGN + DbConstant.TRAN_SACTION_MANAGER)
	public PlatformTransactionManager transactionManager(@Qualifier(DbConstant.DB_CONSIGN + DbConstant.DATA_SOURCE) DataSource dataSource, @Qualifier(DbConstant.DB_CONSIGN + DbConstant.SQL_SESSION_FACTORY) SqlSessionFactory sqlSessionFactory) {
		System.out.println(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource() == dataSource);
		return new FtDataSourceTransactionManager(dataSource);
	}

	@Bean(DbConstant.DB_CONSIGN + DbConstant.JDBC_TEMPLATE)
	public JdbcTemplate jdbcTemplate(@Qualifier(DbConstant.DB_CONSIGN + DbConstant.DATA_SOURCE) DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
}
