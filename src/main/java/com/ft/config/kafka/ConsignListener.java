package com.ft.config.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ft.model.mdo.LogDO;
import com.ft.service.LogService;
import com.ft.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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
		Integer id = logService.add(JsonUtil.json2Object(record.value(), new TypeReference<LogDO>() {
		}));
		log.info("超时/错误日志 log id==>{}", id);
	}
}
