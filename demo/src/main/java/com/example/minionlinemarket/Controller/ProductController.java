package com.example.minionlinemarket.Controller;


import com.example.minionlinemarket.Model.Dto.Request.ProductDto;
import com.example.minionlinemarket.Model.Dto.Response.ProductDetailDto;
import com.example.minionlinemarket.Services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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
    public ResponseEntity<ProductDetailDto> addProduct(@PathVariable Long sellerId, @Valid @RequestPart("product") ProductDto productDto,@Valid @RequestPart(value = "file", required = false) MultipartFile image) throws IOException {
        ProductDetailDto addedProduct;
        if (image != null && !image.isEmpty()) {
            addedProduct = productService.save(sellerId, productDto, image.getBytes());
        } else {
            addedProduct = productService.save(sellerId, productDto);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDetailDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        ProductDetailDto updatedProduct = productService.update(id, productDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        ProductDetailDto productToDelete = productService.findById(id);
        productService.delete(productToDelete);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<Set<ProductDetailDto>> getProductsBySellerId(@PathVariable Long sellerId) {
        Set<ProductDetailDto> products = productService.findAllProductsForSpecificSeller(sellerId);
        return ResponseEntity.ok(products);
    }
}