package com.example.minionlinemarket.Services.Imp;

import com.example.minionlinemarket.Repository.sellerRepo;
import com.example.minionlinemarket.Services.SellerService;
import com.example.minionlinemarket.model.Seller;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SellerServiceImp implements SellerService {


    private final sellerRepo sellerRepo;

    @Autowired
    public SellerServiceImp(sellerRepo sellerRepo) {
        this.sellerRepo = sellerRepo;
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
    public List<Seller> findAllPending() {
        return sellerRepo.findAllByisApproved(false);
    }
}
