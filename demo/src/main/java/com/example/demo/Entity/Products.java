package com.example.demo.Entity;

import com.example.demo.Entity.Enums.ProductState;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @Lob
    @Column(name = "image", columnDefinition = "BLOB")
    private byte[] image;

    private String description;
    private double price;

    private String category;


    @ManyToOne
    @JoinColumn(name = "Seller_Id")
    private Seller seller;

    @Enumerated(EnumType.STRING)
    @JsonBackReference
    private ProductState productState;


}
