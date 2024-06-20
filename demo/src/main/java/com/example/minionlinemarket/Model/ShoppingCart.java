package com.example.minionlinemarket.Model;

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
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    private Set<LineItem> lineItems;
}
