package com.example.minionlinemarket.Controller;


import com.example.minionlinemarket.Services.ProductService;
import com.example.minionlinemarket.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok(product);
    }
//    consumes = "application/json"
    @PostMapping(value ="/sellers/{sellerId}",consumes ="multipart/form-data")
    public ResponseEntity<Product> addProduct(@PathVariable Long sellerId,  @RequestPart("product") Product product
            ,@RequestPart(value = "file", required = false) MultipartFile image) throws IOException {
        if(image != null) {
            Product addedProduct = productService.save(sellerId, product,image);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct);
        }else{
            Product addedProduct = productService.save(sellerId, product);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productService.update(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Product productToDelete = productService.findById(id);
        productService.delete(productToDelete);
        return ResponseEntity.noContent().build();
    }
}
