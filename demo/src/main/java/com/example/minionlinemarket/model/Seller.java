package com.example.minionlinemarket.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Set;

import org.hibernate.annotations.BatchSize;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Seller extends MyUser {

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @BatchSize(size = 10)
    private Set<Product> products;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    @JsonManagedReference
    private Set<myOrder> myOrders;

    private boolean isApproved;
}
