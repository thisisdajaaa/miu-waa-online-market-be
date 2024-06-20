package com.example.minionlinemarket.Services;

import com.example.minionlinemarket.Model.Dto.Request.SellerDto;
import com.example.minionlinemarket.Model.Dto.Response.SellerDetailDto;

import java.util.List;

public interface SellerService {
    List<SellerDetailDto> findAll();
    SellerDetailDto findById(Long id);
    SellerDetailDto save(SellerDto sellerDto);
    void delete(SellerDetailDto sellerDetailDto);
    SellerDetailDto update(Long id, SellerDto sellerDto);
    void deleteOrder(Long id);
    List<SellerDetailDto> findAllPending();
    void approveSeller(Long id);
    void disapproveSeller(Long id);
}
