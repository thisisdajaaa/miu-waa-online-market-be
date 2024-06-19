package com.example.minionlinemarket.Controller;

import com.example.minionlinemarket.Services.OrderService;
import com.example.minionlinemarket.model.myOrder;
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
    public ResponseEntity<List<myOrder>> getAllOrders() {
        List<myOrder> orders = orderService.findAll();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<myOrder> getOrderById(@PathVariable("id") Long id) {
        myOrder order = orderService.findById(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PostMapping("/seller/{sellerId}")
    public ResponseEntity<myOrder> createOrder(@PathVariable("sellerId") Long sellerId, @RequestBody myOrder myOrder) {
        myOrder savedOrder = orderService.save(sellerId, myOrder);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<myOrder> updateOrder(@PathVariable("id") Long id, @RequestBody myOrder updatedOrder) {
        myOrder order = orderService.update(id, updatedOrder);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") Long id) {
        myOrder order = orderService.findById(id);
        orderService.delete(order);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<Set<myOrder>> getOrdersBySellerId(@PathVariable("sellerId") Long sellerId) {
        Set<myOrder> orders = orderService.findOrderBySellerId(sellerId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<Set<myOrder>> getOrdersByBuyerId(@PathVariable("buyerId") Long buyerId) {
        Set<myOrder> orders = orderService.findOrderByBuyerId(buyerId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
