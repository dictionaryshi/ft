package com.ft.config.db;

import com.ft.constant.DbConstant;
import com.ft.db.model.DruidDataSourceDO;
import com.ft.db.model.DynamicDataSource;
import com.ft.db.plugin.FtDataSourceTransactionManager;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * 常量数据库配置
 *
 * @author shichunyang
 */
@Configuration
@PropertySource(value = "classpath:/application.yml")
public class ConsignDataSourceConfig {

	@Bean(DbConstant.DB_CONSIGN + "DataSourceMaster")
	@ConfigurationProperties(prefix = "druid." + DbConstant.DB_CONSIGN + ".master")
	public DruidDataSourceDO consignDataSourceMaster() {
		return new DruidDataSourceDO();
	}

	@Bean(DbConstant.DB_CONSIGN + "DataSourceSlave1")
	@ConfigurationProperties(prefix = "druid." + DbConstant.DB_CONSIGN + ".slave1")
	public DruidDataSourceDO consignDataSourceSlave1() {
		return new DruidDataSourceDO();
	}

	@Bean(DbConstant.DB_CONSIGN + "DataSourceSlave2")
	@ConfigurationProperties(prefix = "druid." + DbConstant.DB_CONSIGN + ".slave2")
	public DruidDataSourceDO consignDataSourceSlave2() {
		return new DruidDataSourceDO();
	}

	@Bean(DbConstant.DB_CONSIGN + "DataSource")
	public DataSource consignDataSource(
			@Qualifier(DbConstant.DB_CONSIGN + "DataSourceMaster") DruidDataSourceDO consignDataSourceMaster,
			@Qualifier(DbConstant.DB_CONSIGN + "DataSourceSlave1") DruidDataSourceDO consignDataSourceSlave1,
			@Qualifier(DbConstant.DB_CONSIGN + "DataSourceSlave2") DruidDataSourceDO consignDataSourceSlave2
	) {
		return DynamicDataSource.instance(consignDataSourceMaster, consignDataSourceSlave1, consignDataSourceSlave2);
	}

	@Bean(DbConstant.DB_CONSIGN + "TransactionManager")
	public PlatformTransactionManager transactionManager(@Qualifier(DbConstant.DB_CONSIGN + "DataSource") DataSource dataSource, @Qualifier(DbConstant.DB_CONSIGN + "SqlSessionFactory") SqlSessionFactory consignSqlSessionFactory) {
		// System.out.println(consignSqlSessionFactory.getConfiguration().getEnvironment().getDataSource() == dataSource);
		return new FtDataSourceTransactionManager(dataSource);
	}

	@Bean(DbConstant.DB_CONSIGN + "JdbcTemplate")
	public JdbcTemplate jdbcTemplate(@Qualifier(DbConstant.DB_CONSIGN + "DataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
}
