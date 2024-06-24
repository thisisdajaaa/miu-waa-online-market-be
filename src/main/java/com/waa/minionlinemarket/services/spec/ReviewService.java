package com.waa.minionlinemarket.services.spec;

import com.waa.minionlinemarket.models.dtos.requests.ReviewDto;
import com.waa.minionlinemarket.models.dtos.responses.ReviewDetailDto;

import java.util.List;
import java.util.Set;

public interface ReviewService {
    List<ReviewDetailDto> getReviews();

    ReviewDetailDto addReview(Long buyerId, Long productId, ReviewDto reviewDto);

    ReviewDetailDto updateReview(Long id, ReviewDto reviewDto);

    void deleteReview(ReviewDetailDto reviewDetailDto);

    Set<ReviewDetailDto> getReviewsForSpecificProduct(Long productId);

    ReviewDetailDto findById(Long id);

    List<ReviewDetailDto> getInappropriateReviews();

    Set<ReviewDetailDto> getReviewsByBuyerId(Long buyerId);
}