package com.example.minionlinemarket.Controller;

import com.example.minionlinemarket.Model.Dto.Request.BuyerDto;
import com.example.minionlinemarket.Model.Dto.Response.BuyerDetailDto;
import com.example.minionlinemarket.Model.Dto.Request.LineItemDto;
import com.example.minionlinemarket.Model.Dto.Response.LineItemDetailDto;
import com.example.minionlinemarket.Model.Dto.Request.AddressDto;
import com.example.minionlinemarket.Model.Dto.Response.AddressDetailDto;
import com.example.minionlinemarket.Model.Dto.Request.PaymentDto;
import com.example.minionlinemarket.Model.Dto.Response.PaymentDetailDto;
import com.example.minionlinemarket.Model.Dto.Request.OrderDto;
import com.example.minionlinemarket.Model.Dto.Response.OrderDetailDto;
import com.example.minionlinemarket.Model.Dto.Response.ShoppingCartDetailDto;
import com.example.minionlinemarket.Model.Dto.Request.ReviewDto;
import com.example.minionlinemarket.Model.Dto.Response.ReviewDetailDto;
import com.example.minionlinemarket.Services.AddressService;
import com.example.minionlinemarket.Services.BuyerService;
import com.example.minionlinemarket.Services.LineItemService;
import com.example.minionlinemarket.Services.PaymentService;
import com.example.minionlinemarket.Services.OrderService;
import com.example.minionlinemarket.Services.ReviewService;
import com.example.minionlinemarket.Services.ShoppingCartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/buyers")
public class BuyerController {

    @Autowired
    private BuyerService buyerService;

    @Autowired
    private LineItemService lineItemService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ShoppingCartService shoppingCartService;

//    @PostMapping
//    public ResponseEntity<BuyerDetailDto> registerBuyer(@RequestBody BuyerDto buyerDto) {
//        return ResponseEntity.ok(buyerService.save(buyerDto));
//    }

    @PreAuthorize("hasAnyAuthority('ADMIN','BUYER')")
    @GetMapping("getBuyerDetails/{id}")
    public ResponseEntity<BuyerDetailDto> getBuyerById(@PathVariable Long id) {
        return ResponseEntity.ok(buyerService.findById(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<BuyerDetailDto>> getAllBuyers() {
        return ResponseEntity.ok(buyerService.findAll());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','BUYER')")
    @DeleteMapping("Delet/{id}")
    public ResponseEntity<Void> deleteBuyer(@PathVariable Long id) {
        buyerService.deleteBuyer(id);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasAuthority('BUYER')")
    @GetMapping("/{buyerId}/shopping-cart")
    public ResponseEntity<ShoppingCartDetailDto> getShoppingCartByBuyerId(@PathVariable Long buyerId) {
        return ResponseEntity.ok(shoppingCartService.getShoppingCartByBuyerId(buyerId));
    }
    @PreAuthorize("hasAuthority('BUYER')")
    @PostMapping("/{buyerId}/shopping-cart/line-items")
    public ResponseEntity<LineItemDetailDto> addLineItemToCart(@PathVariable Long buyerId,
            @RequestBody LineItemDto lineItemRequestDto) {
        return ResponseEntity.ok(lineItemService.addLineItemToCart(buyerId, lineItemRequestDto));
    }

    @PreAuthorize("hasAuthority('BUYER')")
    @PutMapping("/{buyerId}/shopping-cart/line-items/{lineItemId}")
    public ResponseEntity<LineItemDetailDto> updateLineItemInCart(@PathVariable Long buyerId,
            @PathVariable Long lineItemId, @RequestBody LineItemDto lineItemRequestDto) {
        return ResponseEntity.ok(lineItemService.updateLineItemInCart(buyerId, lineItemId, lineItemRequestDto));
    }

    @PreAuthorize("hasAuthority('BUYER')")
    @DeleteMapping("/{buyerId}/shopping-cart/line-items/{lineItemId}")
    public ResponseEntity<Void> removeLineItemFromCart(@PathVariable Long buyerId, @PathVariable Long lineItemId) {
        lineItemService.removeLineItemFromCart(buyerId, lineItemId);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasAuthority('BUYER')")
    @PostMapping("/{buyerId}/Adding/addresses")
    public ResponseEntity<AddressDetailDto> addAddress(@PathVariable Long buyerId, @RequestBody AddressDto addressDto) {
        return ResponseEntity.ok(addressService.addAddress(buyerId, addressDto));
    }

    @PreAuthorize("hasAnyAuthority('SELLER','BUYER')")
    @GetMapping("/{buyerId}/addresses")
    public ResponseEntity<Set<AddressDetailDto>> getAddresses(@PathVariable Long buyerId) {
        return ResponseEntity.ok(addressService.getAddressesByBuyerId(buyerId));
    }


    @PreAuthorize("hasAuthority('BUYER')")
    @PostMapping("/{buyerId}/orders/{orderId}/payments")
    public ResponseEntity<PaymentDetailDto> createPayment(@PathVariable Long buyerId, @PathVariable Long orderId,
            @RequestBody PaymentDto paymentDto) {
        return ResponseEntity.ok(paymentService.makePayment(orderId, paymentDto));
    }

    @PreAuthorize("hasAuthority('BUYER')")
    @PostMapping(value = "/{buyerId}/makeneworder", consumes = "application/json;charset=UTF-8", produces = "application/json")
    public ResponseEntity<OrderDetailDto> placeOrder(@PathVariable Long buyerId, @RequestBody OrderDto orderDto) {
        return ResponseEntity.ok(orderService.placeOrder(buyerId, orderDto));
    }


    @PreAuthorize("hasAuthority('BUYER')")
    @GetMapping("/{buyerId}/orders")
    public ResponseEntity<Set<OrderDetailDto>> getOrderHistory(@PathVariable Long buyerId) {
        return ResponseEntity.ok(orderService.getOrdersByBuyerId(buyerId));
    }

    @PreAuthorize("hasAuthority('BUYER')")
    @PutMapping("/{buyerId}/orders/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long buyerId, @PathVariable Long orderId) {
        orderService.cancelOrder(buyerId, orderId);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasAuthority('BUYER')")
    @PostMapping("/{buyerId}/reviews/products/{productId}")
    public ResponseEntity<ReviewDetailDto> writeReview(@PathVariable Long buyerId, @PathVariable Long productId,
            @RequestBody ReviewDto reviewDto) {
        return ResponseEntity.ok(reviewService.addReview(buyerId, productId, reviewDto));
    }



    @PreAuthorize("hasAnyAuthority('ADMIN','SELLER','BUYER')")
    @GetMapping("/{buyerId}/reviews")
    public ResponseEntity<Set<ReviewDetailDto>> getReviewsByBuyer(@PathVariable Long buyerId) {
        return ResponseEntity.ok(reviewService.getReviewsByBuyerId(buyerId));
    }
}
