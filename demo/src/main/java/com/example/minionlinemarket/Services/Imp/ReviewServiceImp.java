package com.example.minionlinemarket.Services.Imp;

import com.example.minionlinemarket.Repository.ProductRepo;
import com.example.minionlinemarket.Repository.ReviewRepo;
import com.example.minionlinemarket.Services.ProductService;
import com.example.minionlinemarket.Services.ReviewService;
import com.example.minionlinemarket.model.Review;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@Transactional
public class ReviewServiceImp implements ReviewService {

    private ReviewRepo reviewRepo;
    private ProductService productService;

    @Autowired
    public ReviewServiceImp(ReviewRepo reviewRepo, ProductService productService) {
        this.reviewRepo = reviewRepo;
        this.productService = productService;
    }


    @Override
    public List<Review> getReviews() {
        return reviewRepo.findAll();
    }

    @Override
    public Review addReview(Long id,Review review) {
        review.setProduct(productService.findById(id));
        return reviewRepo.save(review);
    }

    @Override
    public Review updateReview(Long id, Review review) {
            Review existingReview = reviewRepo.findById(Math.toIntExact(id)).get();

            // Update fields if new value is not null
            if (review.getContent() != null) {
                existingReview.setContent(review.getContent());
            }
            if (review.getRating() != 0) { // Assuming rating cannot be null, adjust condition as needed
                existingReview.setRating(review.getRating());
            }
            // Update relationships if necessary
            if (review.getProduct() != null) {
                existingReview.setProduct(review.getProduct());
            }
            if (review.getBuyer() != null) {
                existingReview.setBuyer(review.getBuyer());
            }


            // Save the updated review
            return existingReview;

    }

    @Override
    public void deleteReview(Review review) {
         reviewRepo.delete(review);
    }

    @Override
    public Set<Review> getReviewsforSpacificProduct(Long Id) {
        return productService.findById(Id).getReviews();
    }

    @Override
    public Review findById(Long id) {
        return  reviewRepo.findById(Math.toIntExact(id)).orElseThrow(() -> new ResourceNotFoundException("review not found with ID: " + id));
    }


}
