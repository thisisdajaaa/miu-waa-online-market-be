package com.example.minionlinemarket.Repository;

import com.example.minionlinemarket.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Integer> {
    List<Review> findAllByisFlagged(boolean flagged);
}
