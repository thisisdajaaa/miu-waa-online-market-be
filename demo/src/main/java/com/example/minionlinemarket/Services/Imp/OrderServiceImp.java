package com.example.minionlinemarket.Services.Imp;

import com.example.minionlinemarket.Config.MapperConfiguration;
import com.example.minionlinemarket.Model.Dto.Request.OrderDto;
import com.example.minionlinemarket.Model.Dto.Response.OrderDetailDto;
import com.example.minionlinemarket.Model.Seller;
import com.example.minionlinemarket.Repository.OrderRepo;
import com.example.minionlinemarket.Services.OrderService;
import com.example.minionlinemarket.Services.SellerService;
import com.example.minionlinemarket.Model.MyOrder;
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
        MyOrder order = orderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        Hibernate.initialize(order.getSeller());  // Initialize seller if needed
        return mapperConfiguration.convert(order, OrderDetailDto.class);
    }

    @Override
    public OrderDetailDto save(OrderDto orderDto) {
        MyOrder order = mapperConfiguration.convert(orderDto, MyOrder.class);
        Seller seller = mapperConfiguration.convert(sellerService.findById(orderDto.getSellerId()), Seller.class);
        order.setSeller(seller);
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

        String[] fieldNames = {"Date", "Total amount", "Shipping address", "Billing address"};
        String[] fieldValues = {order.getOrderDate().toString(), String.valueOf(order.getTotalAmount()), order.getShippingAddress(), order.getBillingAddress()};

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
}
