package com.kafka.app.payments.service;

import com.kafka.app.core.dto.Payment;

import java.util.List;

public interface PaymentService {
    List<Payment> findAll();

    Payment process(Payment payment);
}
