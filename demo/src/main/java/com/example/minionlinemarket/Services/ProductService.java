package com.example.minionlinemarket.Services;


import com.example.minionlinemarket.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ProductService {

    List<Product> findAll();
    Product findById(Long id);
    Product save(Long id, Product product, MultipartFile image)throws IOException;
    void delete(Product product);
    Product update(Long id ,Product product);
    Product save(Long id, Product product);

    Set<Product> findAllProductsForSpacificSeller(Long id);

}
