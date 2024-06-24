package com.waa.minionlinemarket.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private MyOrder order;

    private String paymentMethod;
    private double amount;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
}
