package com.example.minionlinemarket.Repository;

import com.example.minionlinemarket.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface sellerRepo extends JpaRepository<Seller, Integer> {
}
