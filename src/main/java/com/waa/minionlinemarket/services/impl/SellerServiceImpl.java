package com.waa.minionlinemarket.services.impl;

import com.waa.minionlinemarket.configurations.MapperConfiguration;
import com.waa.minionlinemarket.models.dtos.requests.SellerDto;
import com.waa.minionlinemarket.models.dtos.responses.SellerDetailDto;
import com.waa.minionlinemarket.repositories.OrderRepository;
import com.waa.minionlinemarket.repositories.SellerRepository;
import com.waa.minionlinemarket.services.spec.SellerService;
import com.waa.minionlinemarket.models.OrderStatus;
import com.waa.minionlinemarket.models.Seller;
import com.waa.minionlinemarket.models.MyOrder;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepo;
    private final OrderRepository orderRepo;
    private final MapperConfiguration mapperConfiguration;

    @Autowired
    public SellerServiceImpl(SellerRepository sellerRepo, OrderRepository orderRepo, MapperConfiguration mapperConfiguration) {
        this.sellerRepo = sellerRepo;
        this.orderRepo = orderRepo;
        this.mapperConfiguration = mapperConfiguration;
    }

    @Override
    public List<SellerDetailDto> findAll() {
        return sellerRepo.findAll().stream()
                .map(seller -> {
                    Hibernate.initialize(seller.getProducts());
                    Hibernate.initialize(seller.getMyOrders());
                    return mapperConfiguration.convert(seller, SellerDetailDto.class);
                })
                .collect(Collectors.toList());
    }

    @Override
    public SellerDetailDto findById(Long id) {
        Seller seller = sellerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + id));
        Hibernate.initialize(seller.getProducts());
        Hibernate.initialize(seller.getMyOrders());
        return mapperConfiguration.convert(seller, SellerDetailDto.class);
    }



    @Override
    public SellerDetailDto save(SellerDto sellerDto) {
        Seller seller = mapperConfiguration.convert(sellerDto, Seller.class);
        Seller savedSeller = sellerRepo.save(seller);
        return mapperConfiguration.convert(savedSeller, SellerDetailDto.class);
    }

    @Override
    public SellerDetailDto update(Long id, SellerDto sellerDto) {
        Seller existingSeller = sellerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + id));
        mapperConfiguration.modelMapper().map(sellerDto, existingSeller);
        Seller updatedSeller = sellerRepo.save(existingSeller);
        return mapperConfiguration.convert(updatedSeller, SellerDetailDto.class);
    }

    @Override
    public void delete(SellerDetailDto sellerDetailDto) {
        Seller seller = mapperConfiguration.convert(sellerDetailDto, Seller.class);
        sellerRepo.delete(seller);
    }


    @Override
    public List<SellerDetailDto> findAllPending() {
        return sellerRepo.findAllByIsApproved(false).stream()
                .map(seller -> {
                    Hibernate.initialize(seller.getProducts());
                    Hibernate.initialize(seller.getMyOrders());
                    return mapperConfiguration.convert(seller, SellerDetailDto.class);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void approveSeller(Long id) {
        Seller seller = sellerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + id));
        seller.setApproved(true);
        sellerRepo.save(seller);
    }

    @Override
    public void disapproveSeller(Long id) {
        Seller seller = sellerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + id));
        delete(mapperConfiguration.convert(seller, SellerDetailDto.class));
    }

    @Override
    public int numberofproducts(Long id) {
        return findById(id).getProducts().size();
    }

    @Override
    public void deleteOrder(Long id) {
        MyOrder order = orderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        if (order.getStatus() == OrderStatus.PLACED) {
            order.setStatus(OrderStatus.CANCELED);
        }
    }
}

