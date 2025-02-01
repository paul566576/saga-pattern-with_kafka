package com.kafka.app.orders.saga;

import com.kafka.app.core.dto.commands.*;
import com.kafka.app.core.dto.events.*;
import com.kafka.app.core.types.OrderStatus;
import com.kafka.app.orders.service.OrderHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
@KafkaListener(topics = { "${events.topic.name.orders}",
		"${events.topic.name.products}",
		"${events.topic.name.payments}" })
@RequiredArgsConstructor
public class OrderSaga
{
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final Environment environment;
	private final OrderHistoryService orderHistoryService;

	@KafkaHandler
	public void handleEvent(@Payload final OrderCreatedEvent event)
	{
		final ReservedProductCommand command = new ReservedProductCommand(event.getProductId(), event.getProductQuantity(),
				event.getOrderId());

		kafkaTemplate.send(environment.getProperty("products.commands.topic.name", "products-commands"),
				command);
		orderHistoryService.add(event.getOrderId(), OrderStatus.CREATED);
	}

	@KafkaHandler
	public void handleEvent(@Payload final ProductReservedEvent event)
	{
		final ProcessPaymentCommand command = new ProcessPaymentCommand(
				event.getOrderId(), event.getProductId(), event.getProductPrice(), event.getProductQuantity());
		kafkaTemplate.send(environment.getProperty("payments.commands.topic.name", "payments-commands"),
				command);
	}

	@KafkaHandler
	public void handleEvent(@Payload final PaymentProcessedEvent event)
	{
		final ApproveOrderCommand command = new ApproveOrderCommand(event.getOrderId());
		kafkaTemplate.send(environment.getProperty("orders.commands.topic.name", "orders-commands"),
				command);
	}

	@KafkaHandler
	public void handleEvent(@Payload final OrderApprovedEvent event)
	{
		orderHistoryService.add(event.getOrderId(), OrderStatus.APPROVED);
	}

	@KafkaHandler
	public void handleEvent(@Payload final PaymentFailedEvent event)
	{
		final CancelProductReservationCommand command = new CancelProductReservationCommand(
				event.getOrderId(), event.getProductId(), event.getProductQuantity()
		);
		kafkaTemplate.send(environment.getProperty("products.commands.topic.name", "products-commands"),
				command);
	}

	@KafkaHandler
	public void handlerEvent(@Payload final ProductReservationCancelledEvent event)
	{
		final RejectOrderCommand command = new RejectOrderCommand(event.getOrderId());
		kafkaTemplate.send(environment.getProperty("orders.commands.topic.name", "orders-commands"),
				command);
		orderHistoryService.add(event.getOrderId(), OrderStatus.REJECTED);
	}
}
