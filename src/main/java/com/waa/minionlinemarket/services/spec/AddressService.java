package com.waa.minionlinemarket.services.spec;

import java.util.Set;

import com.waa.minionlinemarket.models.dtos.requests.AddressDto;
import com.waa.minionlinemarket.models.dtos.responses.AddressDetailDto;

public interface AddressService {
    AddressDetailDto addAddress(Long buyerId, AddressDto addressDto);

    Set<AddressDetailDto> getAddressesByBuyerId(Long buyerId);
}
