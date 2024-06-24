package com.waa.minionlinemarket.services.spec;

import com.waa.minionlinemarket.models.dtos.requests.BuyerDto;
import com.waa.minionlinemarket.models.dtos.responses.BuyerDetailDto;

import java.util.List;

public interface BuyerService {
    BuyerDetailDto save(BuyerDto buyerDto);
    void deleteBuyer(Long id);
    BuyerDetailDto findById(Long id);
    List<BuyerDetailDto> findAll();
}

