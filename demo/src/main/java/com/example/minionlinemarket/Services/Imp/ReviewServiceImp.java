package com.example.minionlinemarket.Services.Imp;

import com.example.minionlinemarket.config.MapperConfiguration;
import com.example.minionlinemarket.model.Dto.Request.ReviewDto;
import com.example.minionlinemarket.model.Dto.Response.ReviewDetailDto;
import com.example.minionlinemarket.model.Product;
import com.example.minionlinemarket.Repository.ReviewRepo;
import com.example.minionlinemarket.Services.ProductService;
import com.example.minionlinemarket.Services.ReviewService;
import com.example.minionlinemarket.model.Review;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewServiceImp implements ReviewService {

    private final ReviewRepo reviewRepo;
    private final ProductService productService;
    private final MapperConfiguration mapperConfiguration;

    @Autowired
    public ReviewServiceImp(ReviewRepo reviewRepo, ProductService productService, MapperConfiguration mapperConfiguration) {
        this.reviewRepo = reviewRepo;
        this.productService = productService;
        this.mapperConfiguration = mapperConfiguration;
    }

    @Override
    public List<ReviewDetailDto> getReviews() {
        return reviewRepo.findAll().stream()
                .map(review -> mapperConfiguration.convert(review, ReviewDetailDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDetailDto addReview(Long productId, ReviewDto reviewDto) {
        Review review = mapperConfiguration.convert(reviewDto, Review.class);
        Product product = mapperConfiguration.convert(productService.findById(productId), Product.class);
        review.setProduct(product);
        Review savedReview = reviewRepo.save(review);
        return mapperConfiguration.convert(savedReview, ReviewDetailDto.class);
    }

    @Override
    public ReviewDetailDto updateReview(Long id, ReviewDto reviewDto) {
        Review existingReview = reviewRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));
        mapperConfiguration.modelMapper().map(reviewDto, existingReview);
        Review updatedReview = reviewRepo.save(existingReview);
        return mapperConfiguration.convert(updatedReview, ReviewDetailDto.class);
    }

    @Override
    public void deleteReview(ReviewDetailDto reviewDetailDto) {
        Review review = mapperConfiguration.convert(reviewDetailDto, Review.class);
        reviewRepo.delete(review);
    }

    @Override
    public Set<ReviewDetailDto> getReviewsForSpecificProduct(Long productId) {
        Product product = mapperConfiguration.convert(productService.findById(productId), Product.class);
        Hibernate.initialize(product.getReviews());  // Initialize reviews
        return product.getReviews().stream()
                .map(review -> mapperConfiguration.convert(review, ReviewDetailDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public ReviewDetailDto findById(Long id) {
        Review review = reviewRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));
        return mapperConfiguration.convert(review, ReviewDetailDto.class);
    }

    @Override
    public List<ReviewDetailDto> getInappropriateReviews() {
        return reviewRepo.findAllByisFlagged(true).stream()
                .map(review -> mapperConfiguration.convert(review, ReviewDetailDto.class))
                .collect(Collectors.toList());
    }
}