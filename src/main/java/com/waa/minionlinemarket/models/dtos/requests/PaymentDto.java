package com.waa.minionlinemarket.models.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {
    private double amount;
    private String paymentMethod;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
}
