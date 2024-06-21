package com.example.minionlinemarket.Model.Dto.Response;

import com.example.minionlinemarket.Model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDetailDto {
    private Long id;
    private String content;
    private int rating;
    private boolean isFlagged;
    private Date createdDate;
    private String buyer;
    private String product;
}
