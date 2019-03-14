package com.ft.config.kafka;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.ft.db.plugin.AutoSwitchDatasourceInterceptor;
import com.ft.db.plugin.DataSourceAspect;
import com.ft.model.mdo.LogDO;
import com.ft.redis.base.ListOperationsCache;
import com.ft.redis.plugin.KafkaAop;
import com.ft.util.*;
import org.slf4j.MDC;

import java.util.Date;
import java.util.List;

/**
 * KafkaAppender
 *
 * @author shichunyang
 */
public class KafkaAppender extends AppenderBase<ILoggingEvent> {

	private static final String ERROR = "ERROR";

	/**
	 * 收集日志
	 *
	 * @param event 日志事件
	 */
	@Override
	public void append(ILoggingEvent event) {

		String requestId = MDC.get(LogHolder.REQUEST_ID);

		LogDO log = new LogDO();
		log.setTime(event.getTimeStamp());
		log.setTimestamp(DateUtil.date2Str(new Date(log.getTime()), DateUtil.DEFAULT_DATE_FORMAT));
		log.setApplication(event.getLoggerContextVO().getName());
		log.setThread(event.getThreadName());
		log.setLevel(event.getLevel().levelStr);
		log.setLogger(event.getLoggerName());
		log.setMessage(event.getFormattedMessage());

		StackTraceElement stackTraceElement = event.getCallerData()[0];
		log.setMethod(stackTraceElement.getMethodName());
		log.setLineNumber(stackTraceElement.getLineNumber());

		log.setRequestId(requestId);

		if (SpringContextUtil.getApplicationContext() == null) {
			return;
		}

		ListOperationsCache listOperationsCache = SpringContextUtil.getBean("listOperationsCache", ListOperationsCache.class);

		String cost = "cost==>";
		if (log.getLevel().equals(ERROR)) {
			listOperationsCache.leftPushAll(LogDO.LOG_QUEUE, JsonUtil.object2Json(log));
		} else if (log.getMessage().contains(cost) && !event.getLoggerName().equals(DataSourceAspect.class.getName())
				&& !event.getLoggerName().equals(KafkaAop.class.getName())) {
			long targetTime = 10_000;
			if (event.getLoggerName().equals(AutoSwitchDatasourceInterceptor.class.getName())) {
				targetTime = 3_000;
			}
			String regex = cost + RegexUtil.REGEX_NUMBER;
			List<String> finds = RegexUtil.search(log.getMessage(), regex);
			if (StringUtil.isEmpty(finds)) {
				return;
			}
			long number = Long.parseLong(finds.get(0).replace(cost, ""));
			log.setCost(Math.toIntExact(number));
			if (number > targetTime) {
				listOperationsCache.leftPushAll(LogDO.LOG_QUEUE, JsonUtil.object2Json(log));
			}
		}

/*
		ElasticsearchTemplate elasticsearchTemplate = SpringContextUtil.getBean(ElasticsearchTemplate.class);
		IndexQuery indexQuery = new IndexQueryBuilder().withId(CommonUtil.get32UUID()).withObject(log).build();
		elasticsearchTemplate.index(indexQuery);
		*/
	}
}
