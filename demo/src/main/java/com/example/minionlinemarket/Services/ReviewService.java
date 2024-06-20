package com.example.minionlinemarket.Services;

import com.example.minionlinemarket.Model.Dto.Request.ReviewDto;
import com.example.minionlinemarket.Model.Dto.Response.ReviewDetailDto;
import com.example.minionlinemarket.Model.Review;

import java.util.List;
import java.util.Set;

public interface ReviewService {
    List<ReviewDetailDto> getReviews();
    ReviewDetailDto addReview(Long productId, ReviewDto reviewDto);
    ReviewDetailDto updateReview(Long id, ReviewDto reviewDto);
    void deleteReview(ReviewDetailDto reviewDetailDto);
    Set<ReviewDetailDto> getReviewsForSpecificProduct(Long productId);
    ReviewDetailDto findById(Long id);
    List<ReviewDetailDto> getInappropriateReviews();
}