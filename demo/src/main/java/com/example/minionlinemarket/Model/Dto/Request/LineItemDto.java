package com.example.minionlinemarket.Model.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LineItemDto {
    private Long productId;
    private Long shoppingCartId;
    private int quantity;
}
