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
public class BuyerDetailDto {
    private Long id;
    private String name;
    private String password;
    private String email;
    private Set<AddressDetailDto> addresses;
    private Set<OrderDetailDto> orders;
    private Set<ReviewDetailDto> reviews;
    private ShoppingCartDetailDto shoppingCart;
}
