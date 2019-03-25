package com.ft.br.config.db;

import com.ft.db.constant.DbConstant;
import com.ft.db.plugin.db.ConsignDbConfig;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * ConsignMyBatisConfig
 *
 * @author shichunyang
 */
@Slf4j
@Configuration
@Import(ConsignDbConfig.class)
@MapperScan(basePackages = {"com.ft.br.dao"}, sqlSessionFactoryRef = DbConstant.DB_CONSIGN + DbConstant.SQL_SESSION_FACTORY)
public class ConsignMyBatisConfig {
}
