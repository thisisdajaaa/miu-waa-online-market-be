package com.waa.minionlinemarket.models.dtos.requests;

import com.waa.minionlinemarket.models.dtos.responses.ReviewDetailDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private byte[] image;
    private String category;
    private String subcategory;
    private String brand;
    private Double rating;
    private Double discount;
    private Boolean isInStock;
    private String productType;
    private String color;
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

    @JsonIgnore
    private Set<ReviewDetailDto> reviews;
}