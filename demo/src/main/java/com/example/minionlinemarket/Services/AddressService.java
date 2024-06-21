package com.example.minionlinemarket.Services;

import java.util.Set;

import com.example.minionlinemarket.Model.Dto.Request.AddressDto;
import com.example.minionlinemarket.Model.Dto.Response.AddressDetailDto;

public interface AddressService {
    AddressDetailDto addAddress(Long buyerId, AddressDto addressDto);

    Set<AddressDetailDto> getAddressesByBuyerId(Long buyerId);
}
