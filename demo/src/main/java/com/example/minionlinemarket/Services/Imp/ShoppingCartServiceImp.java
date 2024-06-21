package com.example.minionlinemarket.Services.Imp;

import com.example.minionlinemarket.Config.MapperConfiguration;
import com.example.minionlinemarket.Model.Buyer;
import com.example.minionlinemarket.Model.Dto.Response.ShoppingCartDetailDto;
import com.example.minionlinemarket.Model.ShoppingCart;
import com.example.minionlinemarket.Repository.BuyerRepository;
import com.example.minionlinemarket.Repository.ShoppingCartRepo;
import com.example.minionlinemarket.Services.ShoppingCartService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ShoppingCartServiceImp implements ShoppingCartService {

    private final ShoppingCartRepo shoppingCartRepo;
    private final BuyerRepository buyerRepo;
    private final MapperConfiguration mapperConfiguration;

    @Autowired
    public ShoppingCartServiceImp(ShoppingCartRepo shoppingCartRepo, BuyerRepository buyerRepo, MapperConfiguration mapperConfiguration) {
        this.shoppingCartRepo = shoppingCartRepo;
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
