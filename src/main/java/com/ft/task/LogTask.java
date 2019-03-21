package com.ft.task;

import com.ft.redis.base.ListOperationsCache;
import com.ft.redis.model.LogDO;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * LogTask
 *
 * @author shichunyang
 */
/*
@Component
*/
public class LogTask {
	@Resource(name = "listOperationsCache")
	private ListOperationsCache listOperationsCache;

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
