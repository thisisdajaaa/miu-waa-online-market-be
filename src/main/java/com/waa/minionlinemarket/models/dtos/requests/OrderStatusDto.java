package com.waa.minionlinemarket.models.dtos.requests;

import com.waa.minionlinemarket.models.OrderStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusDto {
    private OrderStatus orderStatus;
}
