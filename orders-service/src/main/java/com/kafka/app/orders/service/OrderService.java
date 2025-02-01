package com.kafka.app.orders.service;

import com.kafka.app.core.dto.Order;

import java.util.UUID;


public interface OrderService {
    Order placeOrder(final Order order);

    void approveOrder(final UUID orderId);

    void rejectOrder(final UUID orderId);
}
