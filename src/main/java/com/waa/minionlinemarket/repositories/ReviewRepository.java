package com.waa.minionlinemarket.repositories;

import com.waa.minionlinemarket.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.isFlagged = true")
    List<Review> findAllByFlagged();
}
