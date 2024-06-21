package com.example.minionlinemarket.Model.Dto.Response;

import com.example.minionlinemarket.Model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDto {
    private Long id;
    private Double totalAmount;
    private AddressDetailDto shippingAddress;
    private AddressDetailDto billingAddress;
    private Set<LineItemDetailDto> lineItems;
    private Date orderDate;
    private OrderStatus status;
}
