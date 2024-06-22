package com.example.minionlinemarket.Repository;

import com.example.minionlinemarket.Model.MyOrder;
import com.example.minionlinemarket.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<MyOrder, Long> {
    List<MyOrder> findBySeller_Id(Long id);

    List<MyOrder> findByBuyer_Id(Long id);

    @Query("SELECT l.product FROM MyOrder o JOIN o.lineItems l WHERE l.product.id = :productId")
    Optional<Product> findProduct(@Param("productId") Long productId);
}
