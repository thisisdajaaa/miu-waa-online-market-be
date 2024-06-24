package com.waa.minionlinemarket.repositories;

import com.waa.minionlinemarket.models.Buyer;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {
    @EntityGraph(attributePaths = "orders")
    Optional<Buyer> findById(Long id);
}
