package com.waa.minionlinemarket.models.dtos.requests;

import com.waa.minionlinemarket.models.Role;
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
