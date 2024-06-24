package com.waa.minionlinemarket.models.dtos.requests;

import java.util.Set;

import com.waa.minionlinemarket.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuyerDto {
    private String name;
    private String password;
    private String email;
    private Set<AddressDto> addresses;
    private Role role;
}
