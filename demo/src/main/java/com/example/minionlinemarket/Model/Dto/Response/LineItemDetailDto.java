package com.example.minionlinemarket.Model.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LineItemDetailDto {
    private Long id;
    private ProductDetailDto product;
    private int quantity;
}
