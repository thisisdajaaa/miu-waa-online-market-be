package com.example.minionlinemarket.Repository;

import com.example.minionlinemarket.Model.Buyer;
import com.example.minionlinemarket.Model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartRepo extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByBuyer(Buyer buyer);
}
