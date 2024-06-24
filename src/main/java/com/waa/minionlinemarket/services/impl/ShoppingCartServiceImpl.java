package com.waa.minionlinemarket.services.impl;

import com.waa.minionlinemarket.configurations.MapperConfiguration;
import com.waa.minionlinemarket.models.Buyer;
import com.waa.minionlinemarket.models.dtos.responses.ShoppingCartDetailDto;
import com.waa.minionlinemarket.models.ShoppingCart;
import com.waa.minionlinemarket.repositories.BuyerRepository;
import com.waa.minionlinemarket.services.spec.ShoppingCartService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final BuyerRepository buyerRepo;
    private final MapperConfiguration mapperConfiguration;

    @Autowired
    public ShoppingCartServiceImpl(BuyerRepository buyerRepo, MapperConfiguration mapperConfiguration) {
        this.buyerRepo = buyerRepo;
        this.mapperConfiguration = mapperConfiguration;
    }

    @Override
    public ShoppingCartDetailDto getShoppingCartByBuyerId(Long buyerId) {
        Buyer buyer = buyerRepo.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with ID: " + buyerId));

        ShoppingCart shoppingCart = buyer.getShoppingCart();
        if (shoppingCart == null) {
            throw new ResourceNotFoundException("Shopping cart not found for buyer with ID: " + buyerId);
        }

        Hibernate.initialize(shoppingCart.getLineItems()); // Ensure line items are initialized

        return mapperConfiguration.convert(shoppingCart, ShoppingCartDetailDto.class);
    }
}
