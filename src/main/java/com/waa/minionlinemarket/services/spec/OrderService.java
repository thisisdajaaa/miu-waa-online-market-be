package com.waa.minionlinemarket.services.spec;

import com.waa.minionlinemarket.models.dtos.requests.OrderDto;
import com.waa.minionlinemarket.models.dtos.responses.OrderDetailDto;
import com.waa.minionlinemarket.models.OrderStatus;
import com.itextpdf.text.DocumentException;
import org.springframework.core.io.ByteArrayResource;

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

    // Add the missing method declarations
    OrderDetailDto placeOrder(Long buyerId, OrderDto orderDto);

    Set<OrderDetailDto> getOrdersByBuyerId(Long buyerId);

    void cancelOrder(Long buyerId, Long orderId);

    ByteArrayResource generateReceipt(Long id) throws DocumentException;

    OrderDetailDto updateOrderStatus(Long orderId, OrderStatus status);
}