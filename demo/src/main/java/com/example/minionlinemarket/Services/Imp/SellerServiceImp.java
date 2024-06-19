package com.example.minionlinemarket.Services.Imp;

import com.example.minionlinemarket.Repository.OrderRepo;
import com.example.minionlinemarket.Repository.sellerRepo;
import com.example.minionlinemarket.Services.OrderService;
import com.example.minionlinemarket.Services.SellerService;
import com.example.minionlinemarket.model.OrderStatus;
import com.example.minionlinemarket.model.Seller;
import com.example.minionlinemarket.model.myOrder;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SellerServiceImp implements SellerService {


    private final sellerRepo sellerRepo;
    private final OrderRepo orderRepo;

    @Autowired
    public SellerServiceImp(sellerRepo sellerRepo, OrderRepo orderRepo) {
        this.sellerRepo = sellerRepo;
        this.orderRepo = orderRepo;
    }



    @Override
    public List<Seller> findAll() {
        return sellerRepo.findAll();
    }

    @Override
    public Seller findById(Long id) {
        return sellerRepo.findById(Math.toIntExact(id))
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + id));
    }

    @Override
    public Seller save(Seller seller) {
        return sellerRepo.save(seller);
    }

    @Override
    public void delete(Seller seller) {
        sellerRepo.delete(seller);
    }

    @Override
    public Seller update(Long id, Seller seller) {
            Seller existingSeller = sellerRepo.findById(Math.toIntExact(id)).get();
            if (seller.getUsername() != null) {
                System.out.println("hello iam here");
                existingSeller.setUsername(seller.getUsername());
            }
            if (seller.getEmail() != null) {
                existingSeller.setEmail(seller.getEmail());
            }

            if (seller.getPassword() != null) {
                existingSeller.setEmail(seller.getPassword());
            }

            return existingSeller;

    }

    @Override
    public void deletOrder(Long id) {
        myOrder order=  orderRepo.findById(Math.toIntExact(id)).orElseThrow(() -> new ResourceNotFoundException("Order not found with ID in deletOrder in seller : " + id));
        if(order.getStatus()==OrderStatus.PLACED){
            order.setStatus(OrderStatus.CANCELED);
            return;
        }
        // what should we do here
    }
}
