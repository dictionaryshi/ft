package com.ft.br.task;

import com.ft.dao.stock.model.LogDO;
import com.ft.redis.base.ListOperationsCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * LogTask
 *
 * @author shichunyang
 */
/*
@Component
*/
public class LogTask {
	@Autowired
	private ListOperationsCache<String, String> listOperationsCache;

	/*
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	*/

	@Scheduled(cron = "0/5 * * * * ?")
	public void log() {
		while (true) {
			/*
			Date expireLimit = DateUtil.getDateAddAmount(DateUtil.getCurrentDate(), Calendar.DAY_OF_MONTH, -31);
			long limitTime = expireLimit.getTime();
			DeleteQuery deleteQuery = new DeleteQuery();
			deleteQuery.setIndex("consign");
			deleteQuery.setType("log");
			QueryBuilder queryBuilder = QueryBuilders.rangeQuery("time").lt(limitTime);
			deleteQuery.setQuery(queryBuilder);
			elasticsearchTemplate.delete(deleteQuery);
			*/

			String logJson = listOperationsCache.rightPop(LogDO.LOG_QUEUE);
			if (logJson == null) {
				break;
			}
		}
	}
}
