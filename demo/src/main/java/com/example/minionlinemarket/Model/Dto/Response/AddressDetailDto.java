package com.example.minionlinemarket.Model.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDetailDto {
    private Long id;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
