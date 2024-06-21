package com.example.minionlinemarket.Repository;

import com.example.minionlinemarket.Model.MyOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<MyOrder, Long> {
    List<MyOrder> findBySeller_Id(Long id);

    List<MyOrder> findByBuyer_Id(Long id);
}
