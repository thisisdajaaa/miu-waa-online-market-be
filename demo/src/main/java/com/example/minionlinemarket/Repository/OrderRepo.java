package com.example.minionlinemarket.Repository;

import com.example.minionlinemarket.model.myOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<myOrder, Integer> {
}
