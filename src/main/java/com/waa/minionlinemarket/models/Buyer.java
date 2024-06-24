package com.waa.minionlinemarket.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.BatchSize;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Buyer extends MyUser {
    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    @JsonManagedReference
    private Set<MyOrder> orders = new HashSet<>();

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    @JsonManagedReference
    private Set<Review> reviews = new HashSet<>();

    @OneToOne(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private ShoppingCart shoppingCart;

    @OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Address> addresses = new HashSet<>();
}
