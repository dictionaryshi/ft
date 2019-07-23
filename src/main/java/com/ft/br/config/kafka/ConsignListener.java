package com.ft.br.config.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ft.br.dao.LogMapper;
import com.ft.dao.stock.model.LogDO;
import com.ft.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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

	private LogMapper logMapper;

	public ConsignListener(LogMapper logMapper) {
		this.logMapper = logMapper;
	}

	@KafkaListener(topics = {"${kafka.consign.topic}"}, containerFactory = "concurrentKafkaListenerContainerFactory", groupId = "${kafka.consign.groupId}")
	public void listen(
			ConsumerRecord<String, String> record,
			Acknowledgment acknowledgment
	) {
		try {
			LogDO logDO = JsonUtil.json2Object(record.value(), new TypeReference<LogDO>() {
			});
			if (logDO == null) {
				return;
			}

			logMapper.insertSelective(logDO);
		} finally {
			acknowledgment.acknowledge();
		}
	}
}
