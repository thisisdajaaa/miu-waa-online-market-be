package com.example.minionlinemarket.Model.Dto.Request;

import java.util.Set;

import com.example.minionlinemarket.Model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuyerDto {
    private String username;
    private String password;
    private String email;
    private Set<AddressDto> addresses; // Add this field
    private Role role;
}
