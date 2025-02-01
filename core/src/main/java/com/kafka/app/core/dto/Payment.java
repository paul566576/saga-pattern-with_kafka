package com.kafka.app.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment
{
	private UUID id;
	private UUID orderId;
	private UUID productId;
	private BigDecimal productPrice;
	private Integer productQuantity;
}

