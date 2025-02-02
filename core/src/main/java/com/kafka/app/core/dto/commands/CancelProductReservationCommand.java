package com.kafka.app.core.dto.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelProductReservationCommand
{
	private UUID productId;
	private UUID orderId;
	private Integer productQuantity;
}
