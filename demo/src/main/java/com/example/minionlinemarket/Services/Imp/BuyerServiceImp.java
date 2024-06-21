package com.example.minionlinemarket.Services.Imp;

import com.example.minionlinemarket.Config.MapperConfiguration;
import com.example.minionlinemarket.Model.*;
import com.example.minionlinemarket.Model.Dto.Request.BuyerDto;
import com.example.minionlinemarket.Model.Dto.Response.*;
import com.example.minionlinemarket.Repository.AddressRepository;
import com.example.minionlinemarket.Repository.BuyerRepository;
import com.example.minionlinemarket.Repository.ShoppingCartRepo;
import com.example.minionlinemarket.Services.BuyerService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BuyerServiceImp implements BuyerService {

    private final BuyerRepository buyerRepository;
    private final AddressRepository addressRepository;
    private final ShoppingCartRepo shoppingCartRepo;
    private final MapperConfiguration mapperConfiguration;

    @Autowired
    public BuyerServiceImp(BuyerRepository buyerRepository, AddressRepository addressRepository,
                           ShoppingCartRepo shoppingCartRepo, MapperConfiguration mapperConfiguration) {
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
            addressRepository.save(address); // Ensure you have an AddressRepository for this
        }

        return mapToDetailDto(savedBuyer);
    }

    @Override
    public void deleteBuyer(Long id) {
        Buyer buyer = buyerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with ID: " + id));
        buyerRepository.delete(buyer);
    }

    @Override
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
                // .isFlagged(review.isFlagged()) // Use isFlagged() instead of flagged()
                .build();
    }

    private LineItemDetailDto mapToLineItemDetailDto(LineItem lineItem) {
        System.out.println("Mapping LineItem: " + lineItem);
        System.out.println("Product in LineItem: " + lineItem.getProduct());

        return LineItemDetailDto.builder()
                .id(lineItem.getId())
                .quantity(lineItem.getQuantity())
                .product(mapperConfiguration.convert(lineItem.getProduct(), ProductDetailDto.class)) // Convert product
                                                                                                     // to
                                                                                                     // ProductDetailDto
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
