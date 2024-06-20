package com.example.minionlinemarket.Repository;

import com.example.minionlinemarket.Model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerRepo extends JpaRepository<Seller, Long> {
    List<Seller> findAllByIsApproved(boolean isApproved);
}
