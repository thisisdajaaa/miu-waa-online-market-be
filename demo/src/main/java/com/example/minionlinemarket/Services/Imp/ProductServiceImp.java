package com.example.minionlinemarket.Services.Imp;

import com.example.minionlinemarket.Config.MapperConfiguration;
import com.example.minionlinemarket.Model.Dto.Request.ProductDto;
import com.example.minionlinemarket.Model.Dto.Response.ProductDetailDto;
import com.example.minionlinemarket.Repository.ProductRepo;
import com.example.minionlinemarket.Services.ProductService;
import com.example.minionlinemarket.Services.SellerService;
import com.example.minionlinemarket.Model.Product;
import com.example.minionlinemarket.Model.Seller;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImp implements ProductService {

    private final ProductRepo productRepo;
    private final SellerService sellerService;
    private final MapperConfiguration mapperConfiguration;

    @Autowired
    public ProductServiceImp(ProductRepo productRepo, SellerService sellerService, MapperConfiguration mapperConfiguration) {
        this.productRepo = productRepo;
        this.sellerService = sellerService;
        this.mapperConfiguration = mapperConfiguration;
    }

    @Override
    public List<ProductDetailDto> findAll() {
        return productRepo.findAll().stream()
                .filter(Product::isInStock)
                .map(product -> {
                    Hibernate.initialize(product.getReviews());  // Initialize reviews if needed
                    return mapperConfiguration.convert(product, ProductDetailDto.class);
                })
                .collect(Collectors.toList());
    }

    public Set<ProductDetailDto> findAllProductsForSpecificSeller(Long id, Map<String, String> filters) {
        Seller seller = mapperConfiguration.convert(sellerService.findById(id), Seller.class);
        Hibernate.initialize(seller.getProducts());  // Initialize products

        return seller.getProducts().stream()
                .filter(product -> {
                    boolean matches = true;
                    if (filters.containsKey("name") && filters.get("name") != null && !filters.get("name").isEmpty()) {
                        String formattedProductName = product.getName().toLowerCase().trim();
                        String formattedFilterName = filters.get("name").toLowerCase().trim();
                        matches = matches && formattedProductName.contains(formattedFilterName);
                    }
                    if (filters.containsKey("price") && filters.get("price") != null && !filters.get("price").isEmpty()) {
                        matches = matches && product.getPrice() == Double.parseDouble(filters.get("price"));
                    }
                    if (filters.containsKey("category") && filters.get("category") != null && !filters.get("category").isEmpty()) {
                        matches = matches && product.getCategory().equals(filters.get("category"));
                    }
                    if (filters.containsKey("rating") && filters.get("rating") != null && !filters.get("rating").isEmpty()) {
                        matches = matches && product.getRating() == Double.parseDouble(filters.get("rating"));
                    }
                    return matches;
                })
                .map(product -> mapperConfiguration.convert(product, ProductDetailDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public ProductDetailDto findById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        Hibernate.initialize(product.getReviews());  // Initialize reviews if needed
        return mapperConfiguration.convert(product, ProductDetailDto.class);
    }

    @Override
    public ProductDetailDto save(Long sellerId, ProductDto productDto) {
        Product product = mapperConfiguration.convert(productDto, Product.class);
        Seller seller = mapperConfiguration.convert(sellerService.findById(sellerId), Seller.class);
        product.setSeller(seller);
        Product savedProduct = productRepo.save(product);
        return mapperConfiguration.convert(savedProduct, ProductDetailDto.class);
    }

    @Override
    public ProductDetailDto save(Long sellerId, ProductDto productDto, byte[] image) throws IOException {
        Product product = mapperConfiguration.convert(productDto, Product.class);
        Seller seller = mapperConfiguration.convert(sellerService.findById(sellerId), Seller.class);
        if (image != null && image.length > 0) {
            product.setImage(image);
        }
        product.setSeller(seller);
        Product savedProduct = productRepo.save(product);
        return mapperConfiguration.convert(savedProduct, ProductDetailDto.class);
    }

    @Override
    public void delete(ProductDetailDto productDetailDto) {
        Product product = mapperConfiguration.convert(productDetailDto, Product.class);

        productRepo.delete(product);
    }

    @Override
    public ProductDetailDto update(Long id, ProductDto productDto) {
        Product existingProduct = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        // Manually update the properties
        if (productDto.getName() != null)
            existingProduct.setName(productDto.getName());
        if (productDto.getDescription() != null)
            existingProduct.setDescription(productDto.getDescription());
        if (productDto.getPrice() != null)
            existingProduct.setPrice(productDto.getPrice());
        if (productDto.getCategory() != null)
            existingProduct.setCategory(productDto.getCategory());
        if (productDto.getStockQuantity() != null) {
            existingProduct.setInStock(true);
            existingProduct.setStockQuantity(productDto.getStockQuantity());
        }

        Product updatedProduct = productRepo.save(existingProduct);
        return mapperConfiguration.convert(updatedProduct, ProductDetailDto.class);
    }

    @Override
    public ProductDetailDto update(Long id, ProductDto productDto, byte[] image) throws IOException {
        Product existingProduct = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        // Manually update the properties
        if (productDto.getName() != null)
            existingProduct.setName(productDto.getName());
        if (productDto.getDescription() != null)
            existingProduct.setDescription(productDto.getDescription());
        if (productDto.getPrice() != null)
            existingProduct.setPrice(productDto.getPrice());
        if (productDto.getCategory() != null)
            existingProduct.setCategory(productDto.getCategory());
        if (productDto.getStockQuantity() != null)
            existingProduct.setStockQuantity(productDto.getStockQuantity());

        // Update the image if provided
        if (image != null && image.length > 0) {
            existingProduct.setImage(image);
        }

        Product updatedProduct = productRepo.save(existingProduct);
        return mapperConfiguration.convert(updatedProduct, ProductDetailDto.class);
    }

}