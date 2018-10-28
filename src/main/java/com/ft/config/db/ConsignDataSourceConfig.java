package com.ft.config.db;

import com.alibaba.druid.support.http.ResourceServlet;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.ft.db.model.DruidDataSourceDO;
import com.ft.db.model.DynamicDataSource;
import com.ft.db.plugin.FtDataSourceTransactionManager;
import com.ft.constant.DbConstant;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

	@Bean("statViewServlet")
	public ServletRegistrationBean statViewServlet() {
		ServletRegistrationBean<StatViewServlet> statViewServlet = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
		Map<String, String> parameters = new HashMap<>(16);
		parameters.put(ResourceServlet.PARAM_NAME_USERNAME, "root");
		parameters.put(ResourceServlet.PARAM_NAME_PASSWORD, "naodian12300");
		statViewServlet.setInitParameters(parameters);
		return statViewServlet;
	}

	@Bean("webStatFilter")
	public FilterRegistrationBean webStatFilter() {
		FilterRegistrationBean<WebStatFilter> webStatFilter = new FilterRegistrationBean<>();
		webStatFilter.setFilter(new WebStatFilter());

		Map<String, String> parameters = new HashMap<>(16);
		parameters.put(WebStatFilter.PARAM_NAME_EXCLUSIONS, "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		webStatFilter.setInitParameters(parameters);

		webStatFilter.setUrlPatterns(Collections.singletonList("/*"));
		return webStatFilter;
	}

	@Bean
	public ServletRegistrationBean hystrixMetricsStreamServlet() {
		ServletRegistrationBean<HystrixMetricsStreamServlet> registrationBean = new ServletRegistrationBean<>(new HystrixMetricsStreamServlet());
		registrationBean.addUrlMappings("/actuator/hystrix.stream");
		return registrationBean;
	}
}
