package com.example.minionlinemarket.Controller;


import com.example.minionlinemarket.Model.Dto.Request.SellerDto;
import com.example.minionlinemarket.Model.Dto.Response.SellerDetailDto;
import com.example.minionlinemarket.Services.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SellerController {

    private final SellerService sellerService;

    @Autowired
    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @GetMapping
    public ResponseEntity<List<SellerDetailDto>> getAllSellers() {
        List<SellerDetailDto> sellers = sellerService.findAll();
        return ResponseEntity.ok(sellers);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<SellerDetailDto>> getAllPendingSellers() {
        List<SellerDetailDto> sellers = sellerService.findAllPending();
        return ResponseEntity.ok(sellers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SellerDetailDto> getSellerById(@PathVariable Long id) {
        SellerDetailDto seller = sellerService.findById(id);
        return ResponseEntity.ok(seller);
    }

    @PostMapping
    public ResponseEntity<SellerDetailDto> createSeller(@RequestBody SellerDto sellerDto) {
        SellerDetailDto createdSeller = sellerService.save(sellerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSeller);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SellerDetailDto> updateSeller(@PathVariable Long id, @RequestBody SellerDto sellerDto) {
        SellerDetailDto updatedSeller = sellerService.update(id, sellerDto);
        return ResponseEntity.ok(updatedSeller);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) {
        SellerDetailDto sellerToDelete = sellerService.findById(id);
        sellerService.delete(sellerToDelete);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/deleteorder/{id}")
    public ResponseEntity<Void> deleteSellerOrder(@PathVariable Long id) {
        sellerService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Void> approveSeller(@PathVariable Long id) {
        sellerService.approveSeller(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/disapprove")
    public ResponseEntity<Void> disapproveSeller(@PathVariable Long id) {
        sellerService.disapproveSeller(id);
        return ResponseEntity.noContent().build();
    }
}
