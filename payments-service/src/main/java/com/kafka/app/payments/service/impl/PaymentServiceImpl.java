package com.kafka.app.payments.service.impl;

import com.kafka.app.core.dto.Payment;
import com.kafka.app.payments.dao.jpa.entity.PaymentEntity;
import com.kafka.app.payments.dao.jpa.repository.PaymentRepository;
import com.kafka.app.payments.service.CreditCardProcessorRemoteService;
import com.kafka.app.payments.service.PaymentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PaymentServiceImpl implements PaymentService
{
	public static final String SAMPLE_CREDIT_CARD_NUMBER = "374245455400126";
	private final PaymentRepository paymentRepository;
	private final CreditCardProcessorRemoteService ccpRemoteService;

	public PaymentServiceImpl(PaymentRepository paymentRepository,
			CreditCardProcessorRemoteService ccpRemoteService)
	{
		this.paymentRepository = paymentRepository;
		this.ccpRemoteService = ccpRemoteService;
	}

	@Transactional
	@Override
	public Payment process(final Payment payment)
	{
		final BigDecimal totalPrice = payment.getProductPrice()
				.multiply(new BigDecimal(payment.getProductQuantity()));
		ccpRemoteService.process(new BigInteger(SAMPLE_CREDIT_CARD_NUMBER), totalPrice);
		final PaymentEntity paymentEntity = new PaymentEntity();
		BeanUtils.copyProperties(payment, paymentEntity);
		paymentRepository.save(paymentEntity);

		final var processedPayment = new Payment();
		BeanUtils.copyProperties(payment, processedPayment);
		processedPayment.setId(paymentEntity.getId());
		return processedPayment;
	}

	@Override
	public List<Payment> findAll()
	{
		return paymentRepository.findAll().stream()
				.map(entity -> new Payment(entity.getId(), entity.getOrderId(), entity.getProductId(), entity.getProductPrice(),
						entity.getProductQuantity())
				).collect(Collectors.toList());
	}
}
