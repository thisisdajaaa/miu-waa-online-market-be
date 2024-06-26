package com.waa.minionlinemarket.controllers;

import com.waa.minionlinemarket.models.dtos.requests.OrderDto;
import com.waa.minionlinemarket.models.dtos.requests.OrderStatusDto;
import com.waa.minionlinemarket.models.dtos.responses.OrderDetailDto;
import com.waa.minionlinemarket.services.spec.OrderService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderDetailDto>> getAllOrders() {
        List<OrderDetailDto> orders = orderService.findAll();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SELLER','BUYER')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailDto> getOrderById(@PathVariable("id") Long id) {
        OrderDetailDto order = orderService.findById(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('BUYER')")
    @PostMapping("/create")
    public ResponseEntity<OrderDetailDto> createOrder(@RequestBody OrderDto orderDto) {
        OrderDetailDto savedOrder = orderService.save(orderDto);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('BUYER')")
    @PutMapping("/{id}")
    public ResponseEntity<OrderDetailDto> updateOrder(@PathVariable("id") Long id, @RequestBody OrderDto orderDto) {
        OrderDetailDto order = orderService.update(id, orderDto);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('BUYER','SELLER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") Long id) {
        OrderDetailDto orderToDelete = orderService.findById(id);
        orderService.delete(orderToDelete);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SELLER')")
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<Set<OrderDetailDto>> getOrdersBySellerId(@PathVariable("sellerId") Long sellerId) {
        Set<OrderDetailDto> orders = orderService.findOrderBySellerId(sellerId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('BUYER','ADMIN')")
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<Set<OrderDetailDto>> getOrdersByBuyerId(@PathVariable("buyerId") Long buyerId) {
        Set<OrderDetailDto> orders = orderService.findOrderByBuyerId(buyerId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('BUYER','ADMIN','SELLER')")
    @GetMapping("/receipt/{id}")
    public ResponseEntity<ByteArrayResource> generateReceipt(@PathVariable("id") Long id) throws DocumentException,
            IOException {
        ByteArrayResource resource = orderService.generateReceipt(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=order_receipt.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @PreAuthorize("hasAnyAuthority('SELLER','ADMIN')")
    @PutMapping("/myorderstatus/{orderId}")
    public ResponseEntity<OrderDetailDto> updateOrderStatus(@PathVariable("orderId") Long orderId,
                                                            @RequestBody OrderStatusDto status)
    {
        OrderDetailDto order = orderService.updateOrderStatus(orderId, status.getOrderStatus());
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

}
