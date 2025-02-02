package com.kafka.app.core.dto.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessPaymentCommand
{
	private UUID orderId;
	private UUID productId;
	private BigDecimal productPrice;
	private Integer productQuantity;
}
