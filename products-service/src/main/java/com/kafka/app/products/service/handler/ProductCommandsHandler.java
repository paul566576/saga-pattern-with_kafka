package com.kafka.app.products.service.handler;

import com.kafka.app.core.dto.Product;
import com.kafka.app.core.dto.commands.CancelProductReservationCommand;
import com.kafka.app.core.dto.commands.ReservedProductCommand;
import com.kafka.app.core.dto.events.ProductReservationCancelledEvent;
import com.kafka.app.core.dto.events.ProductReservationFailedEvent;
import com.kafka.app.core.dto.events.ProductReservedEvent;
import com.kafka.app.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
@KafkaListener(topics = "${products.commands.topic.name}")
@RequiredArgsConstructor
@Slf4j
public class ProductCommandsHandler
{
	private final ProductService productService;
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final Environment environment;

	@KafkaHandler
	public void handleCommand(@Payload final ReservedProductCommand command)
	{
		try
		{
			final Product desiredProduct = new Product(command.getProductId(), command.getProductQuantity());
			final Product reservedProduct = productService.reserve(desiredProduct, command.getOrderId());
			final ProductReservedEvent productReservedEvent = new ProductReservedEvent(command.getOrderId(),
					command.getProductId(),
					reservedProduct.getPrice(),
					command.getProductQuantity());

			kafkaTemplate.send(environment.getProperty("products.events.topic.name",
					"products-events"), productReservedEvent);
		}
		catch (final Exception e)
		{
			log.error(e.getMessage(), e);
			final ProductReservationFailedEvent productReservationFailedEvent = new ProductReservationFailedEvent(
					command.getProductId(), command.getOrderId(), command.getProductQuantity());
			kafkaTemplate.send(environment.getProperty("products.events.topic.name",
					"products-events"), productReservationFailedEvent);
		}
	}

	@KafkaHandler
	public void handleCommand(@Payload final CancelProductReservationCommand command)
	{
		final Product productToCancel = new Product(command.getProductId(), command.getProductQuantity());
		productService.cancelReservation(productToCancel, command.getOrderId());
		final ProductReservationCancelledEvent productCancelledEvent = new ProductReservationCancelledEvent(command.getProductId(),
				command.getOrderId());
		kafkaTemplate.send(environment.getProperty("products.events.topic.name",
				"products-events"), productCancelledEvent);
	}
}
