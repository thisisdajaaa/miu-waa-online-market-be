package com.example.minionlinemarket.Services;

import com.example.minionlinemarket.Model.Dto.Request.OrderDto;
import com.example.minionlinemarket.Model.Dto.Response.OrderDetailDto;
import com.example.minionlinemarket.Model.MyOrder;

import java.util.List;
import java.util.Set;

public interface OrderService {
    List<OrderDetailDto> findAll();
    OrderDetailDto findById(Long id);
    OrderDetailDto save(OrderDto orderDto);
    void delete(OrderDetailDto orderDetailDto);
    Set<OrderDetailDto> findOrderBySellerId(Long id);
    Set<OrderDetailDto> findOrderByBuyerId(Long id);
    OrderDetailDto update(Long id, OrderDto orderDto);
}