package com.example.minionlinemarket.Controller;


import com.example.minionlinemarket.Model.Dto.Request.ProductDto;
import com.example.minionlinemarket.Model.Dto.Response.ProductDetailDto;
import com.example.minionlinemarket.Services.ProductService;
import com.example.minionlinemarket.Model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<List<ProductDetailDto>> getAllProducts() {
        List<ProductDetailDto> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDto> getProductById(@PathVariable Long id) {
        ProductDetailDto product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping(value = "/sellers/{sellerId}", consumes = "multipart/form-data")
    public ResponseEntity<ProductDetailDto> addProduct(@PathVariable Long sellerId, @RequestPart("product") ProductDto productDto, @RequestPart(value = "file", required = false) MultipartFile image) throws IOException {
        ProductDetailDto addedProduct;
        if (image != null && !image.isEmpty()) {
            addedProduct = productService.save(sellerId, productDto, image.getBytes());
        } else {
            addedProduct = productService.save(sellerId, productDto);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct);
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        ProductDetailDto productToDelete = productService.findById(id);
        productService.delete(productToDelete);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<Set<ProductDetailDto>> getProductsBySeller(
            @PathVariable Long sellerId,
            @RequestParam Map<String, String> filters) {
        Set<ProductDetailDto> products = productService.findAllProductsForSpecificSeller(sellerId, filters);
        return ResponseEntity.ok(products);
    }
}