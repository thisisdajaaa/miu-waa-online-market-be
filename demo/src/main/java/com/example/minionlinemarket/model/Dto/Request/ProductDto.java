package com.example.minionlinemarket.model.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private String name;
    private String description;
    private double price;
    private int stockQuantity;
    private byte[] image;
    private String category;
    private String subcategory;
    private String brand;
    private double rating;
    private double discount;
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
}