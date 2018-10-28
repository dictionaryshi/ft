package com.ft.config.kafka;

import com.ft.redis.util.KafkaUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * ConsignProducer
 *
 * @author shichunyang
 */
@Configuration
@EnableKafka
public class ConsignKafkaConfig {

	@Value("${kafka.servers}")
	private String servers;
	@Value("${kafka.consign.topic}")
	private String topic;
	@Value("${kafka.consign.groupId}")
	private String groupId;

	@Bean("consignKafkaTemplate")
	public KafkaTemplate<String, String> kafkaTemplate() {
		return KafkaUtil.getKafkaTemplate(KafkaUtil.producerConfig(servers));
	}

	@Bean("consignConcurrentKafkaListenerContainerFactory")
	public ConcurrentKafkaListenerContainerFactory<String, String> concurrentKafkaListenerContainerFactory() {
		return KafkaUtil.getConcurrentKafkaListenerContainerFactory(KafkaUtil.consumerConfig(servers, groupId));
	}

	@Bean("consignListener")
	public ConsignListener consignListener() {
		return new ConsignListener();
	}
}
