package com.example.minionlinemarket.Services;

import com.example.minionlinemarket.model.Review;

import java.util.List;
import java.util.Set;

public interface ReviewService {
    List<Review> getReviews();
    Review addReview(Long id,Review review);
    Review updateReview(Long id,Review review);
    void deleteReview(Review review);
    Set<Review> getReviewsforSpacificProduct(Long Id);
    Review findById(Long Id);


}
