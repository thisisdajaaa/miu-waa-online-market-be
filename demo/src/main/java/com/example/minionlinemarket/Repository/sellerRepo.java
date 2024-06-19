package com.example.minionlinemarket.Repository;

import com.example.minionlinemarket.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface sellerRepo extends JpaRepository<Seller, Integer> {
    List<Seller> findAllByisApproved(boolean isApproved);
}
