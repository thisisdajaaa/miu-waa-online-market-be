package com.waa.minionlinemarket.services.spec;

import com.waa.minionlinemarket.models.dtos.requests.LineItemDto;
import com.waa.minionlinemarket.models.dtos.responses.LineItemDetailDto;

public interface LineItemService {
    LineItemDetailDto addLineItemToCart(Long buyerId, LineItemDto lineItemDto);

    LineItemDetailDto updateLineItemInCart(Long buyerId, Long lineItemId, LineItemDto lineItemDto);

    void removeLineItemFromCart(Long buyerId, Long lineItemId);
}
