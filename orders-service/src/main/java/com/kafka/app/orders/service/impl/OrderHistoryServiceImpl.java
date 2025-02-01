package com.kafka.app.orders.service.impl;

import com.kafka.app.core.types.OrderStatus;
import com.kafka.app.orders.dao.jpa.entity.OrderHistoryEntity;
import com.kafka.app.orders.dao.jpa.repository.OrderHistoryRepository;
import com.kafka.app.orders.dto.OrderHistory;
import com.kafka.app.orders.service.OrderHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderHistoryServiceImpl implements OrderHistoryService
{
    private final OrderHistoryRepository orderHistoryRepository;

    @Override
    public void add(final UUID orderId, final OrderStatus orderStatus) {
        final OrderHistoryEntity entity = new OrderHistoryEntity();
        entity.setOrderId(orderId);
        entity.setStatus(orderStatus);
        entity.setCreatedAt(new Timestamp(new Date().getTime()));
        orderHistoryRepository.save(entity);
    }

    @Override
    public List<OrderHistory> findByOrderId(final UUID orderId) {
        var entities = orderHistoryRepository.findByOrderId(orderId);
        return entities.stream().map(entity -> {
            OrderHistory orderHistory = new OrderHistory();
            BeanUtils.copyProperties(entity, orderHistory);
            return orderHistory;
        }).toList();
    }
}
