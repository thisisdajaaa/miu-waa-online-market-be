package com.waa.minionlinemarket.models.dtos.requests;

import com.waa.minionlinemarket.models.dtos.responses.AddressDetailDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OrderDto {
    private Long buyerId;
    private Long sellerId;
    private Date orderDate;
    private double totalAmount;
    private AddressDetailDto shippingAddress;
    private AddressDetailDto billingAddress;
}