package com.waa.minionlinemarket.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartDetailDto {
    private Long id;
    private Set<LineItemDetailDto> lineItems;
}
