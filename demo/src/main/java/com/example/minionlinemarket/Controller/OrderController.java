package com.example.minionlinemarket.Controller;

import com.example.minionlinemarket.Model.Dto.Request.OrderDto;
import com.example.minionlinemarket.Model.Dto.Response.OrderDetailDto;
import com.example.minionlinemarket.Services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<List<OrderDetailDto>> getAllOrders() {
        List<OrderDetailDto> orders = orderService.findAll();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailDto> getOrderById(@PathVariable("id") Long id) {
        OrderDetailDto order = orderService.findById(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PostMapping("/ss")
    public void get(@RequestBody OrderDto orderDto){
        System.out.println("hello  "+orderDto.getOrderDate());
    }

    @PostMapping("/creat")
    public ResponseEntity<OrderDetailDto> createOrder(@RequestBody OrderDto orderDto) {
        System.out.println("hello  "+orderDto.getOrderDate());
        OrderDetailDto savedOrder = orderService.save(orderDto);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDetailDto> updateOrder(@PathVariable("id") Long id, @RequestBody OrderDto orderDto) {
        OrderDetailDto order = orderService.update(id, orderDto);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") Long id) {
        OrderDetailDto orderToDelete = orderService.findById(id);
        orderService.delete(orderToDelete);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<Set<OrderDetailDto>> getOrdersBySellerId(@PathVariable("sellerId") Long sellerId) {
        Set<OrderDetailDto> orders = orderService.findOrderBySellerId(sellerId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<Set<OrderDetailDto>> getOrdersByBuyerId(@PathVariable("buyerId") Long buyerId) {
        Set<OrderDetailDto> orders = orderService.findOrderByBuyerId(buyerId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
