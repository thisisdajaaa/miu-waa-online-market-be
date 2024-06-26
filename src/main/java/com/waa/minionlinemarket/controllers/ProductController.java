package com.waa.minionlinemarket.controllers;

import com.waa.minionlinemarket.models.dtos.requests.ProductDto;
import com.waa.minionlinemarket.models.dtos.responses.ProductDetailDto;
import com.waa.minionlinemarket.services.spec.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','BUYER','SELLER')")
    @GetMapping
    public ResponseEntity<List<ProductDetailDto>> getAllProducts(
            @RequestParam(value = "category", required = false) String category) {
        List<ProductDetailDto> products = productService.findAll(category);
        return ResponseEntity.ok(products);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','BUYER','SELLER')")
    @GetMapping("/getSingleProduct/{id}")
    public ResponseEntity<ProductDetailDto> getProductById(@PathVariable Long id) {
        ProductDetailDto product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    @PreAuthorize("hasAnyAuthority('SELLER','BUYER')")
    @PostMapping(value = "/sellers/{sellerId}", consumes = "multipart/form-data")
    public ResponseEntity<ProductDetailDto> addProduct(@PathVariable Long sellerId,
            @Valid @RequestPart("product") ProductDto productDto,
            @Valid @RequestPart(value = "file", required = false) MultipartFile image) throws IOException {
        ProductDetailDto addedProduct;
        if (image != null && !image.isEmpty()) {
            addedProduct = productService.save(sellerId, productDto, image.getBytes());
        } else {
            addedProduct = productService.save(sellerId, productDto);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct);
    }

    @PreAuthorize("hasAuthority('SELLER')")
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ProductDetailDto> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") ProductDto productDto,
            @RequestPart(value = "file", required = false) MultipartFile image) throws IOException {
        ProductDetailDto updatedProduct;
        if (image != null && !image.isEmpty()) {
            updatedProduct = productService.update(id, productDto, image.getBytes());
        } else {
            updatedProduct = productService.update(id, productDto);
        }
        return ResponseEntity.ok(updatedProduct);
    }

    @PreAuthorize("hasAuthority('SELLER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        ProductDetailDto productToDelete = productService.findById(id);
        productService.delete(productToDelete);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SELLER', 'BUYER')")
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<Set<ProductDetailDto>> getProductsBySeller(
            @PathVariable Long sellerId,
            @RequestParam Map<String, String> filters) {
        Set<ProductDetailDto> products = productService.findAllProductsForSpecificSeller(sellerId, filters);
        return ResponseEntity.ok(products);
    }
}