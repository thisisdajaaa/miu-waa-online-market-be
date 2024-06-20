package com.example.minionlinemarket.Services.Imp;

import com.example.minionlinemarket.config.MapperConfiguration;
import com.example.minionlinemarket.model.Dto.Request.OrderDto;
import com.example.minionlinemarket.model.Dto.Response.OrderDetailDto;
import com.example.minionlinemarket.model.Seller;
import com.example.minionlinemarket.Repository.OrderRepo;
import com.example.minionlinemarket.Services.OrderService;
import com.example.minionlinemarket.Services.SellerService;
import com.example.minionlinemarket.model.myOrder;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImp implements OrderService {
    private final OrderRepo orderRepo;
    private final SellerService sellerService;
    private final MapperConfiguration mapperConfiguration;

    @Autowired
    public OrderServiceImp(OrderRepo orderRepo, SellerService sellerService, MapperConfiguration mapperConfiguration) {
        this.orderRepo = orderRepo;
        this.sellerService = sellerService;
        this.mapperConfiguration = mapperConfiguration;
    }

    @Override
    public List<OrderDetailDto> findAll() {
        return orderRepo.findAll().stream()
                .map(order -> mapperConfiguration.convert(order, OrderDetailDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDetailDto findById(Long id) {
        myOrder order = orderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        Hibernate.initialize(order.getSeller());  // Initialize seller if needed
        return mapperConfiguration.convert(order, OrderDetailDto.class);
    }

    @Override
    public OrderDetailDto save(OrderDto orderDto) {
        myOrder order = mapperConfiguration.convert(orderDto, myOrder.class);
        Seller seller = mapperConfiguration.convert(sellerService.findById(orderDto.getSellerId()), Seller.class);
        order.setSeller(seller);
        com.example.minionlinemarket.model.myOrder savedOrder = orderRepo.save(order);
        return mapperConfiguration.convert(savedOrder, OrderDetailDto.class);
    }

    @Override
    public OrderDetailDto update(Long id, OrderDto orderDto) {
        myOrder existingOrder = orderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        mapperConfiguration.modelMapper().map(orderDto, existingOrder);
        myOrder updatedOrder = orderRepo.save(existingOrder);
        return mapperConfiguration.convert(updatedOrder, OrderDetailDto.class);
    }

    @Override
    public void delete(OrderDetailDto orderDetailDto) {
        myOrder order = mapperConfiguration.convert(orderDetailDto, myOrder.class);
        orderRepo.delete(order);
    }

    @Override
    public Set<OrderDetailDto> findOrderBySellerId(Long id) {
        Seller seller = mapperConfiguration.convert(sellerService.findById(id), Seller.class);
        Hibernate.initialize(seller.getMyOrders());
        return seller.getMyOrders().stream()
                .map(order -> mapperConfiguration.convert(order, OrderDetailDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<OrderDetailDto> findOrderByBuyerId(Long id) {
        // Implement buyer logic
        return Set.of();
    }
}
