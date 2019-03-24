package com.ft.br.config.db;

import com.ft.db.constant.DbConstant;
import com.ft.db.dbutil.TxDataSourcePool;
import com.ft.db.dbutil.TxQueryRunner;
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
	public TxDataSourcePool txDataSourcePool(@Qualifier(DbConstant.DB_CONSIGN + DbConstant.DATA_SOURCE) DataSource dataSource) {
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
