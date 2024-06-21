package com.example.minionlinemarket.Model.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import com.example.minionlinemarket.Model.OrderStatus;
import com.example.minionlinemarket.Model.Dto.Response.AddressDetailDto;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long buyerId;
    private Long sellerId;
    private OrderStatus status;
    private double totalAmount;
    private Date orderDate;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private AddressDetailDto shippingAddress;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private AddressDetailDto billingAddress;
}
