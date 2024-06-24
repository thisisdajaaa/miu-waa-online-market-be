package com.waa.minionlinemarket.services.spec;


import com.waa.minionlinemarket.models.dtos.requests.ProductDto;
import com.waa.minionlinemarket.models.dtos.responses.ProductDetailDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProductService {
    List<ProductDetailDto> findAll(String category);
    Set<ProductDetailDto> findAllProductsForSpecificSeller(Long sellerId, Map<String, String> filters);
    ProductDetailDto findById(Long id);
    ProductDetailDto save(Long sellerId, ProductDto productDto);
    ProductDetailDto save(Long sellerId, ProductDto productDto, byte[] image) throws IOException;
    void delete(ProductDetailDto productDetailDto);
    ProductDetailDto update(Long id, ProductDto productDto);
    ProductDetailDto update(Long id, ProductDto productDto, byte[] image) throws IOException;
}