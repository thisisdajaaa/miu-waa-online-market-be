package com.waa.minionlinemarket.services.spec;

import com.waa.minionlinemarket.models.dtos.responses.ShoppingCartDetailDto;

public interface ShoppingCartService {
    ShoppingCartDetailDto getShoppingCartByBuyerId(Long buyerId);
}
