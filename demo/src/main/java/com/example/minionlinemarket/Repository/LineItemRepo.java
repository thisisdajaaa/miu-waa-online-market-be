package com.example.minionlinemarket.Repository;

import com.example.minionlinemarket.model.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineItemRepo extends JpaRepository<LineItem, Integer> {



}
