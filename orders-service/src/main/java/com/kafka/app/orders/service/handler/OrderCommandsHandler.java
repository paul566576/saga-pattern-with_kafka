package com.kafka.app.orders.service.handler;

import com.kafka.app.core.dto.commands.ApproveOrderCommand;
import com.kafka.app.core.dto.commands.RejectOrderCommand;
import com.kafka.app.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
@KafkaListener(topics = { "${commands.topic.name.orders}" })
@RequiredArgsConstructor
public class OrderCommandsHandler
{
	private final OrderService orderService;

	@KafkaHandler
	public void handleCommand(@Payload final ApproveOrderCommand command)
	{
		orderService.approveOrder(command.getOrderId());
	}

	@KafkaHandler
	public void handleCommand(@Payload final RejectOrderCommand command) {
		orderService.rejectOrder(command.getOrderId());
	}
}
