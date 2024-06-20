package com.example.minionlinemarket.model.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long sellerId;
    private String status;
    private Date orderDate;
    private double totalAmount;
    private String shippingAddress;
    private String billingAddress;
}
