package com.example.minionlinemarket.Controller;

import com.example.minionlinemarket.model.Dto.Request.ReviewDto;
import com.example.minionlinemarket.model.Dto.Response.ReviewDetailDto;
import com.example.minionlinemarket.Services.ReviewService;
import com.example.minionlinemarket.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewDetailDto>> getAllReviews() {
        List<ReviewDetailDto> reviews = reviewService.getReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDetailDto> getReviewById(@PathVariable Long id) {
        ReviewDetailDto review = reviewService.findById(id);
        return ResponseEntity.ok(review);
    }

    @PostMapping("/products/{productId}")
    public ResponseEntity<ReviewDetailDto> addReview(@PathVariable Long productId, @RequestBody ReviewDto reviewDto) {
        ReviewDetailDto addedReview = reviewService.addReview(productId, reviewDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedReview);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDetailDto> updateReview(@PathVariable Long id, @RequestBody ReviewDto reviewDto) {
        ReviewDetailDto updatedReview = reviewService.updateReview(id, reviewDto);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        ReviewDetailDto reviewToDelete = reviewService.findById(id);
        reviewService.deleteReview(reviewToDelete);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Set<ReviewDetailDto>> getReviewsForSpecificProduct(@PathVariable Long productId) {
        Set<ReviewDetailDto> reviews = reviewService.getReviewsForSpecificProduct(productId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/inappropriates")
    public ResponseEntity<List<ReviewDetailDto>> getInappropriateReviews() {
        List<ReviewDetailDto> reviews = reviewService.getInappropriateReviews();
        return ResponseEntity.ok(reviews);
    }
}