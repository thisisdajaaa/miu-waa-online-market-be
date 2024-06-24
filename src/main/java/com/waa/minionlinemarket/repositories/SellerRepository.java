package com.waa.minionlinemarket.repositories;

import com.waa.minionlinemarket.models.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    List<Seller> findAllByIsApproved(boolean isApproved);
}
