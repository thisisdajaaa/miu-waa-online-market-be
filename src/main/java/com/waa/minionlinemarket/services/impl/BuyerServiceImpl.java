package com.waa.minionlinemarket.services.impl;

import com.waa.minionlinemarket.configurations.MapperConfiguration;
import com.waa.minionlinemarket.models.*;
import com.waa.minionlinemarket.models.dtos.requests.BuyerDto;
import com.waa.minionlinemarket.models.dtos.responses.*;
import com.waa.minionlinemarket.repositories.AddressRepository;
import com.waa.minionlinemarket.repositories.BuyerRepository;
import com.waa.minionlinemarket.repositories.ShoppingCartRepository;
import com.waa.minionlinemarket.services.spec.BuyerService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BuyerServiceImpl implements BuyerService {

    private final BuyerRepository buyerRepository;
    private final AddressRepository addressRepository;
    private final ShoppingCartRepository shoppingCartRepo;
    private final MapperConfiguration mapperConfiguration;

    @Autowired
    public BuyerServiceImpl(BuyerRepository buyerRepository, AddressRepository addressRepository,
                            ShoppingCartRepository shoppingCartRepo, MapperConfiguration mapperConfiguration) {
        this.buyerRepository = buyerRepository;
        this.shoppingCartRepo = shoppingCartRepo;
        this.mapperConfiguration = mapperConfiguration;
        this.addressRepository = addressRepository;
    }

    @Override
    public BuyerDetailDto save(BuyerDto buyerDto) {
        Buyer buyer = new Buyer();
        buyer.setName(buyerDto.getName());
        buyer.setPassword(buyerDto.getPassword());
        buyer.setEmail(buyerDto.getEmail());
        buyer.setRole(buyerDto.getRole());


        // Ensure collections are initialized
        if (buyer.getAddresses() == null) {
            buyer.setAddresses(new HashSet<>());
        }
        if (buyer.getOrders() == null) {
            buyer.setOrders(new HashSet<>());
        }
        if (buyer.getReviews() == null) {
            buyer.setReviews(new HashSet<>());
        }

        // Add addresses from DTO
        if (buyerDto.getAddresses() != null) {
            Set<Address> addresses = buyerDto.getAddresses().stream()
                    .map(addressDto -> {
                        Address address = new Address();
                        address.setStreet(addressDto.getStreet());
                        address.setCity(addressDto.getCity());
                        address.setState(addressDto.getState());
                        address.setPostalCode(addressDto.getPostalCode());
                        address.setCountry(addressDto.getCountry());
                        address.setBuyer(buyer); // Set the buyer reference
                        return address;
                    })
                    .collect(Collectors.toSet());
            buyer.setAddresses(addresses);
        }

        // Create and set the shopping cart for the buyer
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setBuyer(buyer);
        shoppingCart.setLineItems(new HashSet<>()); // Initialize the line items set
        buyer.setShoppingCart(shoppingCart);

        Buyer savedBuyer = buyerRepository.save(buyer);
        shoppingCartRepo.save(shoppingCart);

        // Save each address explicitly to ensure IDs are generated
        for (Address address : buyer.getAddresses()) {
            addressRepository.save(address);
        }

        return mapToDetailDto(savedBuyer);
    }

    @Override
    public void deleteBuyer(Long id) {
        Buyer buyer = buyerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with ID: " + id));
        buyerRepository.delete(buyer);
    }

    public BuyerDetailDto findById(Long id) {
        Buyer buyer = buyerRepository.findById(id).orElseThrow(() -> new RuntimeException("Buyer not found"));

        Hibernate.initialize(buyer.getAddresses());
        Hibernate.initialize(buyer.getOrders());
        Hibernate.initialize(buyer.getReviews());

        // Ensure collections are initialized
        if (buyer.getAddresses() == null) {
            buyer.setAddresses(new HashSet<>());
        }
        if (buyer.getOrders() == null) {
            buyer.setOrders(new HashSet<>());
        }
        if (buyer.getReviews() == null) {
            buyer.setReviews(new HashSet<>());
        }
        if (buyer.getShoppingCart() == null) {
            buyer.setShoppingCart(new ShoppingCart());
        }

        return mapToDetailDto(buyer);
    }

    @Override
    public List<BuyerDetailDto> findAll() {
        List<Buyer> buyers = buyerRepository.findAll();
        return buyers.stream().map(this::mapToDetailDto).collect(Collectors.toList());
    }

    private BuyerDetailDto mapToDetailDto(Buyer buyer) {
        return BuyerDetailDto.builder()
                .id(buyer.getId())
                .name(buyer.getName())
                .password(buyer.getPassword())
                .email(buyer.getEmail())
                .addresses(buyer.getAddresses().stream().map(this::mapToAddressDetailDto).collect(Collectors.toSet()))
                .orders(buyer.getOrders().stream().map(this::mapToOrderDetailDto).collect(Collectors.toSet()))
                .reviews(buyer.getReviews().stream().map(this::mapToReviewDetailDto).collect(Collectors.toSet()))
                .shoppingCart(mapToShoppingCartDetailDto(buyer.getShoppingCart()))
                .build();
    }

    private AddressDetailDto mapToAddressDetailDto(Address address) {

        if(address==null){
            return null;
        }

        return AddressDetailDto.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .build();
    }

    private OrderDetailDto mapToOrderDetailDto(MyOrder order) {
        return OrderDetailDto.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(mapToAddressDetailDto(order.getShippingAddress())) // Convert Address to
                                                                                    // AddressDetailDto
                .billingAddress(mapToAddressDetailDto(order.getBillingAddress())) // Convert Address to AddressDetailDto
                .lineItems(order.getLineItems().stream().map(this::mapToLineItemDetailDto).collect(Collectors.toSet()))
                .build();
    }

    private ReviewDetailDto mapToReviewDetailDto(Review review) {
        return ReviewDetailDto.builder()
                .id(review.getId())
                .rating(review.getRating())
                .content(review.getContent())
                .build();
    }

    private LineItemDetailDto mapToLineItemDetailDto(LineItem lineItem) {
        ProductDetailDto productDetailDto = mapperConfiguration.convert(lineItem.getProduct(), ProductDetailDto.class);

        if (lineItem.getProduct().getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(lineItem.getProduct().getImage());
            productDetailDto.setBase64Image(base64Image);
        }

        return LineItemDetailDto.builder()
                .id(lineItem.getId())
                .quantity(lineItem.getQuantity())
                .product(productDetailDto)
                .build();
    }

    private ShoppingCartDetailDto mapToShoppingCartDetailDto(ShoppingCart shoppingCart) {
        return ShoppingCartDetailDto.builder()
                .id(shoppingCart.getId())
                .lineItems(shoppingCart.getLineItems() != null ? shoppingCart.getLineItems().stream()
                        .map(this::mapToLineItemDetailDto).collect(Collectors.toSet()) : new HashSet<>())
                .build();
    }

}
