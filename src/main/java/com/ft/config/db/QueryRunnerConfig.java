package com.ft.config.db;

import com.ft.db.dbutil.TxDataSourcePool;
import com.ft.db.dbutil.TxQueryRunner;
import com.ft.constant.DbConstant;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * QueryRunnerConfig
 *
 * @author shichunyang
 */
@Configuration
public class QueryRunnerConfig {

	@Bean
	public TxDataSourcePool txDataSourcePool(@Qualifier(DbConstant.DB_CONSIGN + "DataSource") DataSource dataSource) {
		TxDataSourcePool txDataSourcePool = new TxDataSourcePool();
		txDataSourcePool.setDataSource(dataSource);
		return txDataSourcePool;
	}

	@Bean
	public TxQueryRunner txQueryRunner(TxDataSourcePool txDataSourcePool) {
		TxQueryRunner txQueryRunner = new TxQueryRunner();
		txQueryRunner.setTxDataSourcePool(txDataSourcePool);
		return txQueryRunner;
	}
}
