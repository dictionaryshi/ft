package com.ft.config.kafka;

import com.ft.service.LogService;
import com.ft.util.CommonUtil;
import com.ft.util.JsonUtil;
import com.ft.web.plugin.ControllerAspect;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * ConsignListener
 *
 * @author shichunyang
 */
@Slf4j
public class ConsignListener {

	@Autowired
	private LogService logService;

	@KafkaListener(topics = {"${kafka.consign.topic}"}, containerFactory = "consignConcurrentKafkaListenerContainerFactory", groupId = "${kafka.consign.groupId}")
	public void listen(ConsumerRecord<String, String> record) {
		MDC.put(ControllerAspect.REQUEST_ID, CommonUtil.get32UUID());
		Integer id = logService.add(JsonUtil.json2Object(record.value()));
		log.info("log record==>{}", id);
		MDC.remove(ControllerAspect.REQUEST_ID);
	}
}
