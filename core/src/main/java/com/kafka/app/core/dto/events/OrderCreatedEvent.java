package com.kafka.app.core.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent
{
	private UUID orderId;
	private UUID customerId;
	private UUID productId;
	private Integer productQuantity;
}
