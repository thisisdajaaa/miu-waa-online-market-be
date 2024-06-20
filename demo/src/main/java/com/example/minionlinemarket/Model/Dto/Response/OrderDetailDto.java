package com.example.minionlinemarket.Model.Dto.Response;

import com.example.minionlinemarket.Model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDto {
    private Long id;
    private OrderStatus status;
    private Date orderDate;
    private double totalAmount;
    private String shippingAddress;
    private String billingAddress;
}
