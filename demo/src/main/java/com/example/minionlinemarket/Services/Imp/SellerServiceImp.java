package com.example.minionlinemarket.Services.Imp;

import com.example.minionlinemarket.Config.MapperConfiguration;
import com.example.minionlinemarket.Model.Dto.Request.SellerDto;
import com.example.minionlinemarket.Model.Dto.Response.SellerDetailDto;
import com.example.minionlinemarket.Repository.OrderRepo;
import com.example.minionlinemarket.Repository.SellerRepo;
import com.example.minionlinemarket.Services.SellerService;
import com.example.minionlinemarket.Model.OrderStatus;
import com.example.minionlinemarket.Model.Seller;
import com.example.minionlinemarket.Model.MyOrder;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SellerServiceImp implements SellerService {

    private final SellerRepo sellerRepo;
    private final OrderRepo orderRepo;
    private final MapperConfiguration mapperConfiguration;

    @Autowired
    public SellerServiceImp(SellerRepo sellerRepo, OrderRepo orderRepo, MapperConfiguration mapperConfiguration) {
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
        if (!seller.getProducts().isEmpty() || !seller.getMyOrders().isEmpty()) {
            throw new IllegalStateException("Seller cannot be deleted, has products or orders related to it.");
        }
        delete(mapperConfiguration.convert(seller, SellerDetailDto.class));
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

