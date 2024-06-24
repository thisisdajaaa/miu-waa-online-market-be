package com.waa.minionlinemarket.services.spec;

import com.waa.minionlinemarket.models.dtos.requests.SellerDto;
import com.waa.minionlinemarket.models.dtos.responses.SellerDetailDto;

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
    int numberofproducts(Long id);
}
