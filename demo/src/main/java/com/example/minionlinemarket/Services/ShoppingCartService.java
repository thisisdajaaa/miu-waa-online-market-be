package com.example.minionlinemarket.Services;

import com.example.minionlinemarket.Model.Dto.Response.ShoppingCartDetailDto;

public interface ShoppingCartService {
    ShoppingCartDetailDto getShoppingCartByBuyerId(Long buyerId);
}
