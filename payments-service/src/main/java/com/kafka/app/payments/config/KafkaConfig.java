package com.kafka.app.payments.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;


@Configuration
public class KafkaConfig
{

	@Value("${payments.events.topic.name}")
	private String paymentsEventsTopicname;

	private final static Integer TOPIC_REPLICATION_FACTOR = 3;
	private final static Integer TOPIC_PARTITIONS = 3;

	@Bean
	public KafkaTemplate<String, Object> kafkaTemplate(final ProducerFactory<String, Object> producerFactory)
	{
		return new KafkaTemplate<>(producerFactory);
	}

	@Bean
	public NewTopic createProductsEventsTopic()
	{
		return TopicBuilder.name(paymentsEventsTopicname)
				.partitions(TOPIC_PARTITIONS)
				.replicas(TOPIC_REPLICATION_FACTOR)
				.build();
	}
}
