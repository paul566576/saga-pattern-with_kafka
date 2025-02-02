package com.kafka.app.core.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentProcessedEvent
{
	private UUID orderId;
	private UUID paymentId;
}
