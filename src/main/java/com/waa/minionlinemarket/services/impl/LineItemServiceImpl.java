package com.waa.minionlinemarket.services.impl;

import com.waa.minionlinemarket.configurations.MapperConfiguration;
import com.waa.minionlinemarket.models.Buyer;
import com.waa.minionlinemarket.models.dtos.requests.LineItemDto;
import com.waa.minionlinemarket.models.dtos.responses.LineItemDetailDto;
import com.waa.minionlinemarket.models.LineItem;
import com.waa.minionlinemarket.models.Product;
import com.waa.minionlinemarket.models.ShoppingCart;
import com.waa.minionlinemarket.repositories.BuyerRepository;
import com.waa.minionlinemarket.repositories.LineItemRepository;
import com.waa.minionlinemarket.repositories.ProductRepository;
import com.waa.minionlinemarket.repositories.ShoppingCartRepository;
import com.waa.minionlinemarket.services.spec.LineItemService;
import jakarta.transaction.Transactional;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LineItemServiceImpl implements LineItemService {

    private final LineItemRepository lineItemRepo;
    private final ProductRepository productRepo;
    private final ShoppingCartRepository shoppingCartRepo;
    private final BuyerRepository buyerRepo;
    private final MapperConfiguration mapperConfiguration;

    @Autowired
    public LineItemServiceImpl(LineItemRepository lineItemRepo, ProductRepository productRepo, ShoppingCartRepository shoppingCartRepo, BuyerRepository buyerRepo, MapperConfiguration mapperConfiguration) {
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

        // Check if the line item already exists
        LineItem existingLineItem = shoppingCart.getLineItems().stream()
                .filter(item -> item.getProduct().getId().equals(lineItemDto.getProductId()))
                .findFirst()
                .orElse(null);

        if (existingLineItem != null) {
            // Increment the quantity
            existingLineItem.setQuantity(existingLineItem.getQuantity() + lineItemDto.getQuantity());
            lineItemRepo.save(existingLineItem);
            return mapperConfiguration.convert(existingLineItem, LineItemDetailDto.class);
        } else {
            // Create a new line item
            LineItem lineItem = new LineItem();
            lineItem.setProduct(product);
            lineItem.setQuantity(lineItemDto.getQuantity());
            lineItem.setShoppingCart(shoppingCart);

            shoppingCart.getLineItems().add(lineItem);
            lineItemRepo.save(lineItem);
            return mapperConfiguration.convert(lineItem, LineItemDetailDto.class);
        }
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

        if (lineItem.getQuantity() > 1) {
            // Decrement the quantity
            lineItem.setQuantity(lineItem.getQuantity() - 1);
            lineItemRepo.save(lineItem);
        } else {
            // Remove the line item if quantity is 1
            shoppingCart.getLineItems().remove(lineItem);
            lineItemRepo.delete(lineItem);
        }
    }
}
