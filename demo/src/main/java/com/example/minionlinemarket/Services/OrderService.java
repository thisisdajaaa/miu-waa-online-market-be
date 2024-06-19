package com.example.minionlinemarket.Services;

import com.example.minionlinemarket.model.myOrder;

import java.util.List;
import java.util.Set;

public interface OrderService {
    List<myOrder> findAll();
    myOrder findById(Long id);
    myOrder save(Long sellerid,myOrder myOrder);
    void delete(myOrder myOrder);
    Set<myOrder> findOrderBySellerId(Long Id);
    Set<myOrder> findOrderByBuyerId(Long Id);
    myOrder update(Long id, myOrder myOrder);


}
