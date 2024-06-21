package com.example.minionlinemarket.Model.Dto.Request;

import com.example.minionlinemarket.Model.OrderStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusDto {
    private OrderStatus orderStatus;
}
