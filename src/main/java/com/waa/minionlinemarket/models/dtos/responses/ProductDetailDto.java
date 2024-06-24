package com.waa.minionlinemarket.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private Long sellerId;
    private String category;
    private String subcategory;
    private String brand;
    private Double rating;
    private Double discount;
    private String productType;
    private String color;
    private Boolean isInStock;
    private String sizeOrDimensions;
    private String material;
    private String features;
    private String compatibility;
    private String modelOrYear;
    private String locationOrDeliveryOptions;
    private String paymentOptions;
    private String customerDemographics;
    private String usage;
    private String occasion;
    private Set<ReviewDetailDto> reviews;
    private String base64Image;
    private Boolean isDeletable;
    private String sellerName;
}
