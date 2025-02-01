package com.kafka.app.orders.service;

import com.kafka.app.core.types.OrderStatus;
import com.kafka.app.orders.dto.OrderHistory;

import java.util.List;
import java.util.UUID;

public interface OrderHistoryService {
    void add(UUID orderId, OrderStatus orderStatus);

    List<OrderHistory> findByOrderId(UUID orderId);
}
