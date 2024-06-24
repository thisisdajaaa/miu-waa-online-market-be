package com.waa.minionlinemarket.repositories;

import com.waa.minionlinemarket.models.Buyer;
import com.waa.minionlinemarket.models.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByBuyer(Buyer buyer);
}
