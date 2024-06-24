package com.waa.minionlinemarket.services.impl;

import com.waa.minionlinemarket.configurations.MapperConfiguration;
import com.waa.minionlinemarket.models.dtos.requests.PaymentDto;
import com.waa.minionlinemarket.models.dtos.responses.PaymentDetailDto;
import com.waa.minionlinemarket.models.MyOrder;
import com.waa.minionlinemarket.models.Payment;
import com.waa.minionlinemarket.repositories.OrderRepository;
import com.waa.minionlinemarket.repositories.PaymentRepository;
import com.waa.minionlinemarket.services.spec.PaymentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepo;
    private final OrderRepository orderRepo;
    private final MapperConfiguration mapperConfiguration;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepo, OrderRepository orderRepo, MapperConfiguration mapperConfiguration) {
        this.paymentRepo = paymentRepo;
        this.orderRepo = orderRepo;
        this.mapperConfiguration = mapperConfiguration;
    }

    @Override
    public PaymentDetailDto makePayment(Long orderId, PaymentDto paymentDto) {
        MyOrder order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(paymentDto.getPaymentMethod());
        payment.setAmount(paymentDto.getAmount());
        payment.setCardNumber(paymentDto.getCardNumber());
        payment.setExpiryDate(paymentDto.getExpiryDate());
        payment.setCvv(paymentDto.getCvv());

        order.setPayment(payment);

        Payment savedPayment = paymentRepo.save(payment);
        orderRepo.save(order);

        return mapperConfiguration.convert(savedPayment, PaymentDetailDto.class);
    }
}
