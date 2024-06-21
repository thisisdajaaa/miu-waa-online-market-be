package com.example.minionlinemarket.Services;

import com.example.minionlinemarket.Model.Dto.Request.LineItemDto;
import com.example.minionlinemarket.Model.Dto.Response.LineItemDetailDto;

public interface LineItemService {
    LineItemDetailDto addLineItemToCart(Long buyerId, LineItemDto lineItemDto);

    LineItemDetailDto updateLineItemInCart(Long buyerId, Long lineItemId, LineItemDto lineItemDto);

    void removeLineItemFromCart(Long buyerId, Long lineItemId);
}
