package com.example.minionlinemarket.Model.Dto.Response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerDetailDto {
    private Long id;
    private boolean isApproved;
    private String name;
    private String password;
    private String email;
    private Set<ProductDetailDto> products;
    private Set<OrderDetailDto> myOrders;
}