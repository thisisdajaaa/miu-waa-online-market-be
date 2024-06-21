package com.example.minionlinemarket.Services.Imp;

import com.example.minionlinemarket.Config.MapperConfiguration;
import com.example.minionlinemarket.Model.Buyer;
import com.example.minionlinemarket.Model.Dto.Request.LineItemDto;
import com.example.minionlinemarket.Model.Dto.Response.LineItemDetailDto;
import com.example.minionlinemarket.Model.LineItem;
import com.example.minionlinemarket.Model.Product;
import com.example.minionlinemarket.Model.ShoppingCart;
import com.example.minionlinemarket.Repository.BuyerRepository;
import com.example.minionlinemarket.Repository.LineItemRepo;
import com.example.minionlinemarket.Repository.ProductRepo;
import com.example.minionlinemarket.Repository.ShoppingCartRepo;
import com.example.minionlinemarket.Services.LineItemService;
import jakarta.transaction.Transactional;

import java.util.HashSet;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LineItemServiceImp implements LineItemService {

    private final LineItemRepo lineItemRepo;
    private final ProductRepo productRepo;
    private final ShoppingCartRepo shoppingCartRepo;
    private final BuyerRepository buyerRepo;
    private final MapperConfiguration mapperConfiguration;

    @Autowired
    public LineItemServiceImp(LineItemRepo lineItemRepo, ProductRepo productRepo, ShoppingCartRepo shoppingCartRepo, BuyerRepository buyerRepo, MapperConfiguration mapperConfiguration) {
        this.lineItemRepo = lineItemRepo;
        this.productRepo = productRepo;
        this.shoppingCartRepo = shoppingCartRepo;
        this.buyerRepo = buyerRepo;
        this.mapperConfiguration = mapperConfiguration;
    }

    @Override
    public LineItemDetailDto addLineItemToCart(Long buyerId, LineItemDto lineItemDto) {
        Buyer buyer = buyerRepo.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with ID: " + buyerId));
        
        ShoppingCart shoppingCart = buyer.getShoppingCart();
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
            shoppingCart.setBuyer(buyer);
            buyer.setShoppingCart(shoppingCart);
            shoppingCartRepo.save(shoppingCart);
        }
        
        if (shoppingCart.getLineItems() == null) {
            shoppingCart.setLineItems(new HashSet<>());
        }

        Product product = productRepo.findById(lineItemDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + lineItemDto.getProductId()));

        LineItem lineItem = new LineItem();
        lineItem.setProduct(product);
        lineItem.setQuantity(lineItemDto.getQuantity());
        lineItem.setShoppingCart(shoppingCart);

        shoppingCart.getLineItems().add(lineItem);
        lineItemRepo.save(lineItem);

        return mapperConfiguration.convert(lineItem, LineItemDetailDto.class);
    }

    @Override
    public LineItemDetailDto updateLineItemInCart(Long buyerId, Long lineItemId, LineItemDto lineItemDto) {
        LineItem lineItem = lineItemRepo.findById(lineItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Line item not found with ID: " + lineItemId));

        Product product = productRepo.findById(lineItemDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + lineItemDto.getProductId()));

        lineItem.setQuantity(lineItemDto.getQuantity());
        lineItem.setProduct(product);

        LineItem updatedLineItem = lineItemRepo.save(lineItem);
        return mapperConfiguration.convert(updatedLineItem, LineItemDetailDto.class);
    }

    @Override
    public void removeLineItemFromCart(Long buyerId, Long lineItemId) {
        Buyer buyer = buyerRepo.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with ID: " + buyerId));
        
        ShoppingCart shoppingCart = buyer.getShoppingCart();
        if (shoppingCart == null) {
            throw new ResourceNotFoundException("Shopping cart not found for buyer with ID: " + buyerId);
        }

        LineItem lineItem = lineItemRepo.findById(lineItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Line item not found with ID: " + lineItemId));

        shoppingCart.getLineItems().remove(lineItem);
        lineItemRepo.delete(lineItem);
    }
}
