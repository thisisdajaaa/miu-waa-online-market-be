package com.example.minionlinemarket.Repository;

import com.example.minionlinemarket.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {
}
