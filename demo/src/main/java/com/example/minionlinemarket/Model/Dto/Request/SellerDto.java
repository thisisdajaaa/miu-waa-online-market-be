package com.example.minionlinemarket.Model.Dto.Request;

import com.example.minionlinemarket.Model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerDto {
    private boolean isApproved;
    private String name;
    private String password;
    private String email;
    private Role role;
}
