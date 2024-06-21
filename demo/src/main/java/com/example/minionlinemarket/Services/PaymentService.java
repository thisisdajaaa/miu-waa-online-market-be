package com.example.minionlinemarket.Services;

import com.example.minionlinemarket.Model.Dto.Request.PaymentDto;
import com.example.minionlinemarket.Model.Dto.Response.PaymentDetailDto;

public interface PaymentService {
    PaymentDetailDto makePayment(Long orderId, PaymentDto paymentDto);
}
