package com.example.minionlinemarket.Services;


import com.example.minionlinemarket.Model.Dto.Request.ProductDto;
import com.example.minionlinemarket.Model.Dto.Response.ProductDetailDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProductService {
    List<ProductDetailDto> findAll();
    Set<ProductDetailDto> findAllProductsForSpecificSeller(Long sellerId, Map<String, String> filters);
    ProductDetailDto findById(Long id);
    ProductDetailDto save(Long sellerId, ProductDto productDto);
    ProductDetailDto save(Long sellerId, ProductDto productDto, byte[] image) throws IOException;
    void delete(ProductDetailDto productDetailDto);
    ProductDetailDto update(Long id, ProductDto productDto);
    ProductDetailDto update(Long id, ProductDto productDto, byte[] image) throws IOException;
}