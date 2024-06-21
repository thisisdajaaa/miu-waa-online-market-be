package com.example.minionlinemarket.Services.Imp;

import com.example.minionlinemarket.Config.MapperConfiguration;
import com.example.minionlinemarket.Model.*;
import com.example.minionlinemarket.Model.Dto.Request.OrderDto;
import com.example.minionlinemarket.Model.Dto.Response.AddressDetailDto;
import com.example.minionlinemarket.Model.Dto.Response.LineItemDetailDto;
import com.example.minionlinemarket.Model.Dto.Response.OrderDetailDto;
import com.example.minionlinemarket.Model.Dto.Response.ProductDetailDto;
import com.example.minionlinemarket.Repository.*;
import com.example.minionlinemarket.Services.BuyerService;
import com.example.minionlinemarket.Services.OrderService;
import com.example.minionlinemarket.Services.SellerService;

import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.core.io.ByteArrayResource;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImp implements OrderService {
    private final OrderRepo orderRepo;
    private final SellerService sellerService;
    private final BuyerService buyerService;
    private final ShoppingCartRepo shoppingCartRepo;
    private final MapperConfiguration mapperConfiguration;
    private final AddressRepository addressRepository;
    private final SellerRepo sellerRepo;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    public OrderServiceImp(OrderRepo orderRepo, SellerService sellerService, BuyerService buyerService,
                           ShoppingCartRepo shoppingCartRepo, MapperConfiguration mapperConfiguration,
                           AddressRepository addressRepository, SellerRepo sellerRepo) {
        this.orderRepo = orderRepo;
        this.sellerService = sellerService;
        this.buyerService = buyerService;
        this.shoppingCartRepo = shoppingCartRepo;
        this.mapperConfiguration = mapperConfiguration;
        this.addressRepository = addressRepository;
        this.sellerRepo = sellerRepo;
    }

    @Override
    public List<OrderDetailDto> findAll() {
        return orderRepo.findAll().stream()
                .map(order -> mapperConfiguration.convert(order, OrderDetailDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDetailDto findById(Long id) {
        MyOrder order = orderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        Hibernate.initialize(order.getSeller());
        Hibernate.initialize(order.getBuyer());
        return mapperConfiguration.convert(order, OrderDetailDto.class);
    }

    @Override
    public OrderDetailDto save(OrderDto orderDto) {
        MyOrder order = new MyOrder();
        order.setOrderDate(orderDto.getOrderDate());
        order.setStatus(OrderStatus.PLACED);
        order.setTotalAmount(orderDto.getTotalAmount());

        Seller seller = mapperConfiguration.convert(sellerService.findById(orderDto.getSellerId()), Seller.class);
        Buyer buyer = mapperConfiguration.convert(buyerService.findById(orderDto.getBuyerId()), Buyer.class);
        order.setSeller(seller);
        order.setBuyer(buyer);

        // Save Addresses
        Address shippingAddress = convertToAddress(orderDto.getShippingAddress());
        Address billingAddress = convertToAddress(orderDto.getBillingAddress());

        if (shippingAddress != null) {
            shippingAddress = addressRepository.save(shippingAddress);
        }

        if (billingAddress != null) {
            billingAddress = addressRepository.save(billingAddress);
        }

        order.setShippingAddress(shippingAddress);
        order.setBillingAddress(billingAddress);

        MyOrder savedOrder = orderRepo.save(order);
        return mapperConfiguration.convert(savedOrder, OrderDetailDto.class);
    }

    @Override
    public OrderDetailDto update(Long id, OrderDto orderDto) {
        MyOrder existingOrder = orderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        mapperConfiguration.modelMapper().map(orderDto, existingOrder);
        MyOrder updatedOrder = orderRepo.save(existingOrder);
        return mapperConfiguration.convert(updatedOrder, OrderDetailDto.class);
    }

    @Override
    public void delete(OrderDetailDto orderDetailDto) {
        MyOrder order = mapperConfiguration.convert(orderDetailDto, MyOrder.class);
        orderRepo.delete(order);
    }

    @Override
    public Set<OrderDetailDto> findOrderBySellerId(Long id) {
        List<MyOrder> orders = orderRepo.findBySeller_Id(id);
        return orders.stream()
                .map(order -> mapperConfiguration.convert(order, OrderDetailDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<OrderDetailDto> findOrderByBuyerId(Long id) {
        List<MyOrder> orders = orderRepo.findByBuyer_Id(id);
        return orders.stream()
                .map(order -> mapperConfiguration.convert(order, OrderDetailDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public OrderDetailDto placeOrder(Long buyerId, OrderDto orderDto) {
        Buyer buyer = mapperConfiguration.convert(buyerService.findById(buyerId), Buyer.class);
        ShoppingCart shoppingCart = shoppingCartRepo.findByBuyer(buyer)
                .orElseThrow(() -> new ResourceNotFoundException("Shopping cart not found for buyer with ID: " + buyerId));
        Seller seller = mapperConfiguration.convert(sellerService.findById(orderDto.getSellerId()), Seller.class);

        MyOrder order = new MyOrder();
        order.setBuyer(buyer);
        order.setSeller(seller);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PLACED);
        order.setTotalAmount(shoppingCart.getLineItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum());
        order.setShippingAddress(convertToAddress(orderDto.getShippingAddress()));
        order.setBillingAddress(convertToAddress(orderDto.getBillingAddress()));

        // Save Addresses
        if (order.getShippingAddress() != null) {
            addressRepository.save(order.getShippingAddress());
        }

        if (order.getBillingAddress() != null) {
            addressRepository.save(order.getBillingAddress());
        }

        // Create a new set of line items for the order and update product stock
        Set<LineItem> orderLineItems = new HashSet<>();
        for (LineItem item : shoppingCart.getLineItems()) {
            LineItem newItem = new LineItem();
            Product product = item.getProduct();

            newItem.setProduct(product);
            newItem.setQuantity(item.getQuantity());
            newItem.setOrder(order);
            newItem.setShoppingCart(null);  // Clear the shopping cart ID
            orderLineItems.add(newItem);

            // Update product stock quantity
            int newStockQuantity = product.getStockQuantity() - item.getQuantity();
            product.setStockQuantity(newStockQuantity);
            product.setInStock(newStockQuantity > 0);
        }
        order.setLineItems(orderLineItems);

        MyOrder savedOrder = orderRepo.save(order);

        // Clear shopping cart
        shoppingCart.getLineItems().clear();
        shoppingCartRepo.save(shoppingCart);

        return mapperConfiguration.convert(savedOrder, OrderDetailDto.class);
    }



    @Override
    @Transactional()
    public Set<OrderDetailDto> getOrdersByBuyerId(Long buyerId) {
        Buyer buyer = buyerRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found"));

        // Fetch orders to avoid lazy loading issues
        Hibernate.initialize(buyer.getOrders());

        // Ensure orders set is not null
        Set<MyOrder> orders = buyer.getOrders();
        if (orders == null) {
            orders = new HashSet<>();
        }

        return orders.stream()
                .map(order -> mapperConfiguration.convert(order, OrderDetailDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public void cancelOrder(Long buyerId, Long orderId) {
        MyOrder order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        if (order.getBuyer().getId().equals(buyerId) && order.getStatus() == OrderStatus.PLACED) {
            order.setStatus(OrderStatus.CANCELED);
            orderRepo.save(order);
        } else {
            throw new IllegalStateException("Order cannot be cancelled");
        }
    }

    private Address convertToAddress(AddressDetailDto addressDetailDto) {
        if (addressDetailDto == null) {
            return null;
        }
        Address address = new Address();
        address.setStreet(addressDetailDto.getStreet());
        address.setCity(addressDetailDto.getCity());
        address.setState(addressDetailDto.getState());
        address.setPostalCode(addressDetailDto.getPostalCode());
        address.setCountry(addressDetailDto.getCountry());
        return address;
    }

    private AddressDetailDto mapToAddressDetailDto(Address address) {
        if (address == null) {
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
                .shippingAddress(
                        order.getShippingAddress() != null ? mapToAddressDetailDto(order.getShippingAddress()) : null)
                .billingAddress(
                        order.getBillingAddress() != null ? mapToAddressDetailDto(order.getBillingAddress()) : null)
                .lineItems(order.getLineItems().stream().map(this::mapToLineItemDetailDto).collect(Collectors.toSet()))
                .build();
    }

    private LineItemDetailDto mapToLineItemDetailDto(LineItem lineItem) {
        return LineItemDetailDto.builder()
                .id(lineItem.getId())
                .quantity(lineItem.getQuantity())
                .product(mapperConfiguration.convert(lineItem.getProduct(), ProductDetailDto.class)) // Convert product
                // to
                // ProductDetailDto
                .build();
    }

    @Override
    public ByteArrayResource generateReceipt(Long id) throws DocumentException {
        MyOrder existingOrder = orderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        OrderDetailDto order = mapperConfiguration.convert(existingOrder, OrderDetailDto.class);

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font bodyFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

        Paragraph title = new Paragraph("Receipt for Order ID: " + order.getId(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        String[] fieldNames = { "Date", "Total amount", "Shipping address", "Billing address" };
        String[] fieldValues = { order.getOrderDate().toString(), String.valueOf(order.getTotalAmount()),
                order.getShippingAddress(), order.getBillingAddress() };

        for (int i = 0; i < fieldNames.length; i++) {
            PdfPCell cell = new PdfPCell(new Phrase(fieldNames[i], bodyFont));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
            table.addCell(new Phrase(fieldValues[i], bodyFont));
        }

        document.add(table);
        document.close();

        byte[] bytes = out.toByteArray();

        return new ByteArrayResource(bytes);
    }

    @Override
    public OrderDetailDto updateOrderStatus(Long orderId, OrderStatus status){
        MyOrder order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        order.setStatus(status);
        return mapperConfiguration.convert(orderRepo.save(order), OrderDetailDto.class);
    }
}