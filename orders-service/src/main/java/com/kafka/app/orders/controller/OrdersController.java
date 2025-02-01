package com.kafka.app.orders.controller;

import com.kafka.app.core.dto.Order;
import com.kafka.app.orders.dto.CreateOrderRequest;
import com.kafka.app.orders.dto.CreateOrderResponse;
import com.kafka.app.orders.dto.OrderHistoryResponse;
import com.kafka.app.orders.service.OrderHistoryService;
import com.kafka.app.orders.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrderService orderService;
    private final OrderHistoryService orderHistoryService;


    public OrdersController(OrderService orderService, OrderHistoryService orderHistoryService) {
        this.orderService = orderService;
        this.orderHistoryService = orderHistoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CreateOrderResponse placeOrder(@RequestBody @Valid CreateOrderRequest request) {
        var order = new Order();
        BeanUtils.copyProperties(request, order);
        Order createdOrder = orderService.placeOrder(order);

        var response = new CreateOrderResponse();
        BeanUtils.copyProperties(createdOrder, response);
        return response;
    }

    @GetMapping("/{orderId}/history")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderHistoryResponse> getOrderHistory(@PathVariable UUID orderId) {
        return orderHistoryService.findByOrderId(orderId).stream().map(orderHistory -> {
            OrderHistoryResponse orderHistoryResponse = new OrderHistoryResponse();
            BeanUtils.copyProperties(orderHistory, orderHistoryResponse);
            return orderHistoryResponse;
        }).collect(Collectors.toList());
    }
}
