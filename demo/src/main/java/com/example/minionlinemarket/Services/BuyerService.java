package com.example.minionlinemarket.Services;

import com.example.minionlinemarket.Model.Dto.Request.BuyerDto;
import com.example.minionlinemarket.Model.Dto.Response.BuyerDetailDto;

import java.util.List;

public interface BuyerService {
    BuyerDetailDto save(BuyerDto buyerDto);
    void deleteBuyer(Long id);
    BuyerDetailDto findById(Long id);
    List<BuyerDetailDto> findAll();
}

