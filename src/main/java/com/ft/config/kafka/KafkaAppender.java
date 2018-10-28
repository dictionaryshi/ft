package com.ft.config.kafka;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.ft.util.SpringContextUtil;
import com.ft.web.plugin.ControllerAspect;
import com.ft.model.mdo.LogDO;
import com.ft.dao.LogMapper;
import org.slf4j.MDC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

		String requestId = MDC.get(ControllerAspect.REQUEST_ID);
		String userName = MDC.get(ControllerAspect.USER_NAME);

		LogDO log = new LogDO();
		log.setTime(event.getTimeStamp());
		log.setApplication(event.getLoggerContextVO().getName());
		log.setThread(event.getThreadName());
		log.setLevel(event.getLevel().levelStr);
		log.setLogger(event.getLoggerName());
		log.setMessage(event.getFormattedMessage());

		StackTraceElement stackTraceElement = event.getCallerData()[0];
		log.setMethod(stackTraceElement.getMethodName());
		log.setLineNumber(stackTraceElement.getLineNumber());

		log.setRequestId(requestId);
		log.setUserName(userName);

		if (SpringContextUtil.getApplicationContext() != null) {
			//@SuppressWarnings("unchecked")
			//KafkaTemplate<String, String> kafkaTemplate = SpringContextUtil.getBean("consignKafkaTemplate", KafkaTemplate.class);
			//String topic = SpringContextUtil.getApplicationContext().getEnvironment().getProperty("kafka.consign.topic");
			//kafkaTemplate.send(topic, "log", JsonUtil.object2Json(log));

			String cost = "cost==>";
			if (log.getLevel().equals(ERROR)) {
				LogMapper logMapper = SpringContextUtil.getBean(LogMapper.class);
				logMapper.insertSelective(log);
			} else if (log.getMessage().contains(cost)) {
				long targetTime = 3000;
				String regex = cost + "[\\d]+";
				Matcher matcher = Pattern.compile(regex).matcher(log.getMessage());
				while (matcher.find()) {
					long number = Long.parseLong(matcher.group().replace(cost, ""));
					if (number > targetTime) {
						LogMapper logMapper = SpringContextUtil.getBean(LogMapper.class);
						logMapper.insertSelective(log);
					}
				}
			}
		}
	}
}
