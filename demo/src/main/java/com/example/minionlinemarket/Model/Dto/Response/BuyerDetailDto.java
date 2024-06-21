package com.example.minionlinemarket.Model.Dto.Response;

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
    private String username;
    private String password;
    private String email;
    private Set<AddressDetailDto> addresses;
    private Set<OrderDetailDto> orders;
    private Set<ReviewDetailDto> reviews;
    private ShoppingCartDetailDto shoppingCart;
}
