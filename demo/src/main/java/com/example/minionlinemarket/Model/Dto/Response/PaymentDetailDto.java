package com.example.minionlinemarket.Model.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDetailDto {
    private Long id;
    private double amount;
    private Date paymentDate;
    private String paymentMethod;
}
