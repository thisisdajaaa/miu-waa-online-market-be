package com.example.minionlinemarket.Services.Imp;

import com.example.minionlinemarket.Repository.ProductRepo;
import com.example.minionlinemarket.Services.ProductService;
import com.example.minionlinemarket.Services.SellerService;
import com.example.minionlinemarket.model.Product;
import com.example.minionlinemarket.model.Seller;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class ProductServiceImp implements ProductService {

    private ProductRepo productRepo;
    private SellerService sellerService;



    @Autowired
    public ProductServiceImp(ProductRepo productRepo, SellerService sellerService) {
        this.productRepo = productRepo;
        this.sellerService = sellerService;
    }

    @Override
    public List<Product> findAll() {
        return productRepo.findAll();
    }

    @Override
    public Set<Product> findAllProductsForSpacificSeller(Long id) {
       return sellerService.findById(id).getProducts();
    }



    @Override
    public Product findById(Long id) {
        return productRepo.findById(Math.toIntExact(id)).orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + id));

    }

    @Override
    public Product save(Long id,Product product) {
        Seller seller = sellerService.findById(id);
        product.setSeller(seller);

        return productRepo.save(product);
    }

    @Override
    public void delete(Product product) {
        productRepo.delete(product);
    }

    @Override
    public Product update(Long id,Product product) {
        Optional<Product> optionalProduct = productRepo.findById(Math.toIntExact(id));

        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();
            existingProduct.setId(product.getId());
            if (product.getName() != null) {
                existingProduct.setName(product.getName());
            }
            if (product.getDescription() != null) {
                existingProduct.setDescription(product.getDescription());
            }
            if (product.getImage() != null) {
                existingProduct.setImage(product.getImage());
            }
            if (product.getPrice() != 0.0) { // Assuming price cannot be null, check if 0.0 is appropriate
                existingProduct.setPrice(product.getPrice());
            }
            if (product.getBrand() != null) {
                existingProduct.setBrand(product.getBrand());
            }
            if (product.getCategory() != null) {
                existingProduct.setCategory(product.getCategory());
            }
            if (product.getSubcategory() != null) {
                existingProduct.setSubcategory(product.getSubcategory());
            }
            if (product.getRating() != 0.0) { // Assuming rating cannot be null, check if 0.0 is appropriate
                existingProduct.setRating(product.getRating());
            }
            if (product.getDiscount() != 0.0) { // Assuming discount cannot be null, check if 0.0 is appropriate
                existingProduct.setDiscount(product.getDiscount());
            }
//            existingProduct.setNewArrival(product.isNewArrival());
//            existingProduct.setBestSeller(product.isBestSeller());
//            existingProduct.setInStock(product.isInStock());
            if (product.getProductType() != null) {
                existingProduct.setProductType(product.getProductType());
            }
            if (product.getColor() != null) {
                existingProduct.setColor(product.getColor());
            }
            if (product.getSizeOrDimensions() != null) {
                existingProduct.setSizeOrDimensions(product.getSizeOrDimensions());
            }
            if (product.getMaterial() != null) {
                existingProduct.setMaterial(product.getMaterial());
            }
            if (product.getFeatures() != null) {
                existingProduct.setFeatures(product.getFeatures());
            }
            if (product.getCompatibility() != null) {
                existingProduct.setCompatibility(product.getCompatibility());
            }
            if (product.getModelOrYear() != null) {
                existingProduct.setModelOrYear(product.getModelOrYear());
            }
            if (product.getLocationOrDeliveryOptions() != null) {
                existingProduct.setLocationOrDeliveryOptions(product.getLocationOrDeliveryOptions());
            }
            if (product.getPaymentOptions() != null) {
                existingProduct.setPaymentOptions(product.getPaymentOptions());
            }
            if (product.getCustomerDemographics() != null) {
                existingProduct.setCustomerDemographics(product.getCustomerDemographics());
            }
            if (product.getUsage() != null) {
                existingProduct.setUsage(product.getUsage());
            }
            if (product.getOccasion() != null) {
                existingProduct.setOccasion(product.getOccasion());
            }

            return productRepo.save(existingProduct);
        } else {
            throw new ResourceNotFoundException("Seller not found with ID: " + id);
        }
    }


}
