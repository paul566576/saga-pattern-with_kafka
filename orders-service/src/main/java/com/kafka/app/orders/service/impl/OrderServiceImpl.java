package com.kafka.app.orders.service.impl;

import com.kafka.app.core.dto.Order;
import com.kafka.app.core.dto.events.OrderApprovedEvent;
import com.kafka.app.core.dto.events.OrderCreatedEvent;
import com.kafka.app.core.types.OrderStatus;
import com.kafka.app.orders.dao.jpa.entity.OrderEntity;
import com.kafka.app.orders.dao.jpa.repository.OrderRepository;
import com.kafka.app.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService
{
	private final OrderRepository orderRepository;
	private final KafkaTemplate<String, Object> orderKafkaTemplate;
	private final Environment environment;


	@Override
	public Order placeOrder(final Order order)
	{
		final OrderEntity entity = new OrderEntity();
		entity.setCustomerId(order.getCustomerId());
		entity.setProductId(order.getProductId());
		entity.setProductQuantity(order.getProductQuantity());
		entity.setStatus(OrderStatus.CREATED);
		orderRepository.save(entity);

		final OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(entity.getId(), entity.getCustomerId(),
				entity.getProductId(), entity.getProductQuantity());

		orderKafkaTemplate.send(environment.getProperty("orders.events.topic.name", "orders-events"), orderCreatedEvent);

		return new Order(
				entity.getId(),
				entity.getCustomerId(),
				entity.getProductId(),
				entity.getProductQuantity(),
				entity.getStatus());
	}

	@Override
	public void approveOrder(final UUID orderId)
	{
		final OrderEntity order = orderRepository.findById(orderId).orElse(null);
		Assert.notNull(order, "Order not found with id " + orderId);
		order.setStatus(OrderStatus.APPROVED);
		orderRepository.save(order);

		final OrderApprovedEvent orderApprovedEvent = new OrderApprovedEvent(orderId);
		orderKafkaTemplate.send(environment.getProperty("orders.events.topic.name", "orders-events"),
				orderApprovedEvent);
	}

	@Override
	public void rejectOrder(final UUID orderId)
	{
		final OrderEntity order = orderRepository.findById(orderId).orElse(null);
		Assert.notNull(order, "Order not found with id " + orderId);
		order.setStatus(OrderStatus.REJECTED);
		orderRepository.save(order);


	}





}
