package com.example.minionlinemarket.Services;


import com.example.minionlinemarket.model.Dto.Request.ProductDto;
import com.example.minionlinemarket.model.Dto.Response.ProductDetailDto;
import com.example.minionlinemarket.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ProductService {
    List<ProductDetailDto> findAll();
    Set<ProductDetailDto> findAllProductsForSpecificSeller(Long id);
    ProductDetailDto findById(Long id);
    ProductDetailDto save(Long sellerId, ProductDto productDto);
    ProductDetailDto save(Long sellerId, ProductDto productDto, byte[] image) throws IOException;
    void delete(ProductDetailDto productDetailDto);
    ProductDetailDto update(Long id, ProductDto productDto);
}