package com.waa.minionlinemarket.controllers;


import com.waa.minionlinemarket.models.dtos.requests.SellerDto;
import com.waa.minionlinemarket.models.dtos.responses.SellerDetailDto;
import com.waa.minionlinemarket.services.spec.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<SellerDetailDto>> getAllSellers() {
        List<SellerDetailDto> sellers = sellerService.findAll();
        return ResponseEntity.ok(sellers);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/pending")
    public ResponseEntity<List<SellerDetailDto>> getAllPendingSellers() {
        List<SellerDetailDto> sellers = sellerService.findAllPending();
        return ResponseEntity.ok(sellers);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SELLER')")
    @GetMapping("/numberofproducts/{id}")
    public ResponseEntity<Integer> getnumberofproductforspacificseller(@PathVariable Long id){
        int numberofproduct = sellerService.numberofproducts(id);
        return ResponseEntity.ok(numberofproduct);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SELLER', 'BUYER')")
    @GetMapping("/{id}")
    public ResponseEntity<SellerDetailDto> getSellerById(@PathVariable Long id) {
        SellerDetailDto seller = sellerService.findById(id);
        return ResponseEntity.ok(seller);
    }

    @PreAuthorize("hasAuthority('SELLER')")
    @PutMapping("/{id}")
    public ResponseEntity<SellerDetailDto> updateSeller(@PathVariable Long id, @RequestBody SellerDto sellerDto) {
        SellerDetailDto updatedSeller = sellerService.update(id, sellerDto);
        return ResponseEntity.ok(updatedSeller);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SELLER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) {
        SellerDetailDto sellerToDelete = sellerService.findById(id);
        sellerService.delete(sellerToDelete);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasAuthority('SELLER')")
    @PutMapping("/deleteorder/{id}")
    public ResponseEntity<Void> deleteSellerOrder(@PathVariable Long id) {
        sellerService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<Void> approveSeller(@PathVariable Long id) {
        sellerService.approveSeller(id);
        return ResponseEntity.noContent().build();

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}/disapprove")
    public ResponseEntity<Void> disapproveSeller(@PathVariable Long id) {
        sellerService.disapproveSeller(id);
        return ResponseEntity.noContent().build();
    }
}
