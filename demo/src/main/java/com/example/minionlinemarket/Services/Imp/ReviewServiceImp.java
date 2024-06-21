package com.example.minionlinemarket.Services.Imp;

import com.example.minionlinemarket.Config.MapperConfiguration;
import com.example.minionlinemarket.Model.Dto.Request.ReviewDto;
import com.example.minionlinemarket.Model.Dto.Response.ReviewDetailDto;
import com.example.minionlinemarket.Model.Buyer;
import com.example.minionlinemarket.Model.Product;
import com.example.minionlinemarket.Repository.ReviewRepo;
import com.example.minionlinemarket.Services.BuyerService;
import com.example.minionlinemarket.Services.ProductService;
import com.example.minionlinemarket.Services.ReviewService;
import com.example.minionlinemarket.Model.Review;
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
    private final BuyerService buyerService;

    @Autowired
    public ReviewServiceImp(ReviewRepo reviewRepo, ProductService productService,
            MapperConfiguration mapperConfiguration, BuyerService buyerService) {
        this.reviewRepo = reviewRepo;
        this.productService = productService;
        this.mapperConfiguration = mapperConfiguration;
        this.buyerService = buyerService;
    }

    @Override
    public List<ReviewDetailDto> getReviews() {
        return reviewRepo.findAll().stream()
                .map(review -> {
                    ReviewDetailDto reviewDetailDto = mapperConfiguration.convert(review, ReviewDetailDto.class);
                    reviewDetailDto.setBuyer(review.getBuyer().getName());
                    reviewDetailDto.setProduct(review.getProduct().getName());
                    return reviewDetailDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDetailDto addReview(Long buyerId, Long productId, ReviewDto reviewDto) {
        Review review = mapperConfiguration.convert(reviewDto, Review.class);
        Product product = mapperConfiguration.convert(productService.findById(productId), Product.class);
        Buyer buyer = mapperConfiguration.convert(buyerService.findById(buyerId), Buyer.class);
        review.setProduct(product);
        review.setBuyer(buyer);
        review.setFlagged(reviewDto.getIsFlagged());
        Review savedReview = reviewRepo.save(review);
        ReviewDetailDto reviewDetailDto = mapperConfiguration.convert(savedReview, ReviewDetailDto.class);
        reviewDetailDto.setBuyer(review.getBuyer().getName());
        reviewDetailDto.setProduct(review.getProduct().getName());
        return reviewDetailDto;
    }

    @Override
    public ReviewDetailDto updateReview(Long id, ReviewDto reviewDto) {
        Review existingReview = reviewRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));
        mapperConfiguration.modelMapper().map(reviewDto, existingReview);
        Review updatedReview = reviewRepo.save(existingReview);
        ReviewDetailDto reviewDetailDto = mapperConfiguration.convert(updatedReview, ReviewDetailDto.class);
        reviewDetailDto.setBuyer(existingReview.getBuyer().getName());
        reviewDetailDto.setProduct(existingReview.getProduct().getName());
        return reviewDetailDto;
    }

    @Override
    public void deleteReview(ReviewDetailDto reviewDetailDto) {
        Review review = mapperConfiguration.convert(reviewDetailDto, Review.class);
        reviewRepo.delete(review);
    }

    @Override
    public Set<ReviewDetailDto> getReviewsForSpecificProduct(Long productId) {
        Product product = mapperConfiguration.convert(productService.findById(productId), Product.class);
        Hibernate.initialize(product.getReviews());
        return product.getReviews().stream()
                .map(review -> {
                    ReviewDetailDto reviewDetailDto = mapperConfiguration.convert(review, ReviewDetailDto.class);
                    reviewDetailDto.setBuyer(review.getBuyer().getName());
                    reviewDetailDto.setProduct(review.getProduct().getName());
                    return reviewDetailDto;
                })
                .collect(Collectors.toSet());
    }

    @Override
    public ReviewDetailDto findById(Long id) {
        Review review = reviewRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));
        ReviewDetailDto reviewDetailDto = mapperConfiguration.convert(review, ReviewDetailDto.class);
        reviewDetailDto.setBuyer(review.getBuyer().getName());
        reviewDetailDto.setProduct(review.getProduct().getName());
        return reviewDetailDto;
    }

    @Override
    public List<ReviewDetailDto> getInappropriateReviews() {
        return reviewRepo.findAllByFlagged().stream()
                .map(review -> {
                    ReviewDetailDto reviewDetailDto = mapperConfiguration.convert(review, ReviewDetailDto.class);
                    reviewDetailDto.setBuyer(review.getBuyer().getName());
                    reviewDetailDto.setProduct(review.getProduct().getName());
                    return reviewDetailDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Set<ReviewDetailDto> getReviewsByBuyerId(Long buyerId) {
        Buyer buyer = mapperConfiguration.convert(buyerService.findById(buyerId), Buyer.class);
        Hibernate.initialize(buyer.getReviews());
        return buyer.getReviews().stream()
                .map(review -> {
                    ReviewDetailDto reviewDetailDto = mapperConfiguration.convert(review, ReviewDetailDto.class);
                    reviewDetailDto.setBuyer(review.getBuyer().getName());
                    reviewDetailDto.setProduct(review.getProduct().getName());
                    return reviewDetailDto;
                })
                .collect(Collectors.toSet());
    }
}