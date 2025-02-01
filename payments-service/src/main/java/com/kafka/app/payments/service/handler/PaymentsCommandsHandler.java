package com.kafka.app.payments.service.handler;

import com.kafka.app.core.dto.Payment;
import com.kafka.app.core.dto.commands.ProcessPaymentCommand;
import com.kafka.app.core.dto.events.PaymentFailedEvent;
import com.kafka.app.core.dto.events.PaymentProcessedEvent;
import com.kafka.app.core.exceptions.CreditCardProcessorUnavailableException;
import com.kafka.app.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
@KafkaListener(topics = { "${payments.commands.topic.name}" })
@RequiredArgsConstructor
@Slf4j
public class PaymentsCommandsHandler
{
	private final PaymentService paymentService;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	@Value("${payments.events.topic.name}")
	private String paymentsEventsTopicName;

	@KafkaHandler
	public void handleCommand(@Payload final ProcessPaymentCommand command)
	{
		try
		{
			final Payment payment = new Payment(command.getOrderId(), command.getProductId(), command.getProductPrice(),
					command.getProductQuantity());
			final Payment processedPayment = paymentService.process(payment);
			final PaymentProcessedEvent paymentProcessedEvent = new PaymentProcessedEvent(processedPayment.getOrderId(),
					processedPayment.getId());
			kafkaTemplate.send(paymentsEventsTopicName, paymentProcessedEvent);
		}
		catch (final CreditCardProcessorUnavailableException e)
		{
			log.error(e.getMessage(), e);
			final PaymentFailedEvent paymentFailedEvent = new PaymentFailedEvent(command.getOrderId(), command.getProductId(),
					command.getProductQuantity());
			kafkaTemplate.send(paymentsEventsTopicName, paymentFailedEvent);
		}


	}
}
