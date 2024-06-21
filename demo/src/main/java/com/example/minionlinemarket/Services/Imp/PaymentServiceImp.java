package com.example.minionlinemarket.Services.Imp;

import com.example.minionlinemarket.Config.MapperConfiguration;
import com.example.minionlinemarket.Model.Dto.Request.PaymentDto;
import com.example.minionlinemarket.Model.Dto.Response.PaymentDetailDto;
import com.example.minionlinemarket.Model.MyOrder;
import com.example.minionlinemarket.Model.Payment;
import com.example.minionlinemarket.Repository.OrderRepo;
import com.example.minionlinemarket.Repository.PaymentRepo;
import com.example.minionlinemarket.Services.PaymentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PaymentServiceImp implements PaymentService {

    private final PaymentRepo paymentRepo;
    private final OrderRepo orderRepo;
    private final MapperConfiguration mapperConfiguration;

    @Autowired
    public PaymentServiceImp(PaymentRepo paymentRepo, OrderRepo orderRepo, MapperConfiguration mapperConfiguration) {
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
