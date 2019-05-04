package com.ft.br.config.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ft.br.service.LogService;
import com.ft.redis.model.LogDO;
import com.ft.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * ConsignListener
 *
 * @author shichunyang
 */
@Slf4j
@Component
public class ConsignListener {

	@Autowired
	private LogService logService;

	@KafkaListener(topics = {"${kafka.consign.topic}"}, containerFactory = "concurrentKafkaListenerContainerFactory", groupId = "${kafka.consign.groupId}")
	public void listen(
			ConsumerRecord<String, String> record,
			Acknowledgment acknowledgment
	) {
		try {
			Integer id = logService.add(JsonUtil.json2Object(record.value(), new TypeReference<LogDO>() {
			}));
			log.info("超时/错误日志 log id==>{}", id);
		} finally {
			acknowledgment.acknowledge();
		}
	}
}
