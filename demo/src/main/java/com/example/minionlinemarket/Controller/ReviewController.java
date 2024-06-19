package com.example.minionlinemarket.Controller;

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
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.getReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        Review review = reviewService.findById(id);
        return ResponseEntity.ok(review);
    }

    @PostMapping("/products/{productId}")
    public ResponseEntity<Review> addReview(@PathVariable Long productId, @RequestBody Review review) {
        Review addedReview = reviewService.addReview(productId, review);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedReview);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review review) {
        Review updatedReview = reviewService.updateReview(id, review);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        Review reviewToDelete = reviewService.findById(id);
        reviewService.deleteReview(reviewToDelete);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Set<Review>> getReviewsForSpecificProduct(@PathVariable Long productId) {
        Set<Review> reviews = reviewService.getReviewsforSpacificProduct(productId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/inappropriates")
    public ResponseEntity<List<Review>> getInappropriateReviews() {
        List<Review> reviews = reviewService.getInappropriateReviews();
        return ResponseEntity.ok(reviews);
    }

}
