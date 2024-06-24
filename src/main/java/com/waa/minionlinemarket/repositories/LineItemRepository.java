package com.waa.minionlinemarket.repositories;

import com.waa.minionlinemarket.models.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineItemRepository extends JpaRepository<LineItem, Long> {

}
