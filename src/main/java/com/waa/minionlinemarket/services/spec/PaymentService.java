package com.waa.minionlinemarket.services.spec;

import com.waa.minionlinemarket.models.dtos.requests.PaymentDto;
import com.waa.minionlinemarket.models.dtos.responses.PaymentDetailDto;

public interface PaymentService {
    PaymentDetailDto makePayment(Long orderId, PaymentDto paymentDto);
}
