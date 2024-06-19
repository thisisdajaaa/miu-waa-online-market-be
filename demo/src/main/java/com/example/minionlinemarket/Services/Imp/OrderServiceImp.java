package com.example.minionlinemarket.Services.Imp;

import com.example.minionlinemarket.Repository.OrderRepo;
import com.example.minionlinemarket.Services.OrderService;
import com.example.minionlinemarket.Services.SellerService;
import com.example.minionlinemarket.model.myOrder;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class OrderServiceImp implements OrderService {

    private OrderRepo orderRepo;
    private SellerService sellerService;

    @Autowired
    public OrderServiceImp(OrderRepo orderRepo, SellerService sellerService) {
        this.orderRepo = orderRepo;
        this.sellerService = sellerService;
    }

    @Override
    public List<myOrder> findAll() {
        return orderRepo.findAll();
    }

    @Override
    public myOrder findById(Long id) {
        return orderRepo.findById(Math.toIntExact(id)).orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
    }


    // add the id for seller and id for the buyer also
    @Override
    public myOrder save(Long sellerid,myOrder myOrder) {
        myOrder.setSeller(sellerService.findById(sellerid));
        // add also the buyer
        return orderRepo.save(myOrder);
    }

    @Override
    public void delete(myOrder myOrder) {
        orderRepo.delete(myOrder);
    }

    @Override
    public Set<myOrder> findOrderBySellerId(Long Id) {
        return sellerService.findById(Id).getMyOrders();
    }

    @Override
    public Set<myOrder> findOrderByBuyerId(Long Id) {
        return Set.of();
    }

    @Override
    public myOrder update(Long id, myOrder myOrder) {
        com.example.minionlinemarket.model.myOrder ExistmyOrder = findById(id);
        ExistmyOrder.setStatus(myOrder.getStatus());
        return ExistmyOrder;
    }
}
