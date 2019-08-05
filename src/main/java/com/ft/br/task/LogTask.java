package com.ft.br.task;

import org.springframework.stereotype.Component;

/**
 * LogTask
 *
 * @author shichunyang
 */
@Component
public class LogTask {
/*
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	@Scheduled(cron = "0/60 * * * * ?")
	public void log() {
		Date expireLimit = DateUtil.getDateAddAmount(DateUtil.getCurrentDate(), Calendar.DAY_OF_MONTH, -31);
		long limitTime = expireLimit.getTime();

		DeleteQuery deleteQuery = new DeleteQuery();
		deleteQuery.setIndex(DbConstant.DB_CONSIGN);
		deleteQuery.setType("log");

		QueryBuilder queryBuilder = QueryBuilders.rangeQuery("time").lt(limitTime);
		deleteQuery.setQuery(queryBuilder);

		elasticsearchTemplate.delete(deleteQuery);
	}
*/
}
