package com.kafka.app.orders.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;


@Configuration("ordersKafkaConfig")
public class KafkaConfig
{
	@Value("${events.topic.name.orders}")
	private String ordersEventsTopicName;
	@Value("${commands.topic.name.products}")
	private String productsCommandsTopicName;
	@Value("${commands.topic.name.payments}")
	private String paymentsCommandsTopicName;
	@Value("${commands.topic.name.orders}")
	private String ordersCommandsTopicName;

	private final static Integer TOPIC_REPLICATION_FACTOR = 3;
	private final static Integer TOPIC_PARTITIONS = 3;

	@Bean
	public KafkaTemplate<String, Object> kafkaTemplate(final ProducerFactory<String, Object> producerFactory)
	{
		return new KafkaTemplate<>(producerFactory);
	}

	@Bean
	public NewTopic createOrdersEventsTopic()
	{
		return TopicBuilder.name(ordersEventsTopicName)
				.partitions(TOPIC_PARTITIONS)
				.replicas(TOPIC_REPLICATION_FACTOR)
				.build();
	}

	@Bean
	public NewTopic createProductsCommandsTopic()
	{
		return TopicBuilder.name(productsCommandsTopicName)
				.partitions(TOPIC_PARTITIONS)
				.replicas(TOPIC_REPLICATION_FACTOR)
				.build();
	}

	@Bean
	public NewTopic createPaymentsCommandsTopic()
	{
		return TopicBuilder.name(paymentsCommandsTopicName)
				.partitions(TOPIC_PARTITIONS)
				.replicas(TOPIC_REPLICATION_FACTOR)
				.build();
	}

	@Bean
	public NewTopic createPOrdersCommandsTopic()
	{
		return TopicBuilder.name(ordersCommandsTopicName )
				.partitions(TOPIC_PARTITIONS)
				.replicas(TOPIC_REPLICATION_FACTOR)
				.build();
	}

}
