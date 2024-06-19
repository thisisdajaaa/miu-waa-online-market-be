package com.example.minionlinemarket.Controller;


import com.example.minionlinemarket.Services.SellerService;
import com.example.minionlinemarket.model.Seller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    private final SellerService sellerService;

    @Autowired
    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellers() {
        List<Seller> sellers = sellerService.findAll();
        return ResponseEntity.ok(sellers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) {
        Seller seller = sellerService.findById(id);
        return ResponseEntity.ok(seller);
    }

    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) {
        Seller createdSeller = sellerService.save(seller);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSeller);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Seller> updateSeller(@PathVariable Long id, @RequestBody Seller seller) {
        Seller updatedSeller = sellerService.update(id, seller);
        return ResponseEntity.ok(updatedSeller);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) {
        Seller sellerToDelete = sellerService.findById(id);
        sellerService.delete(sellerToDelete);
        return ResponseEntity.noContent().build();
    }
}
