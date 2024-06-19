package com.example.minionlinemarket.Services;

import com.example.minionlinemarket.model.Seller;

import java.util.List;

public interface SellerService {
    List<Seller> findAll();
    Seller findById(Long id);
    Seller save(Seller seller);
    void delete(Seller seller);
    Seller update(Long id, Seller seller);
    void deletOrder(Long id);


}
