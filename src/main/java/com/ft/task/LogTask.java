package com.ft.task;

import com.ft.model.mdo.LogDO;
import com.ft.redis.base.ListOperationsCache;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * LogTask
 *
 * @author shichunyang
 */
@Component
public class LogTask {
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Value("${kafka.consign.topic}")
	private String logTopic;

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

			kafkaTemplate.send(logTopic, "log_" + RandomUtils.nextDouble(), logJson);
		}
	}
}
