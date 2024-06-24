package com.waa.minionlinemarket.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationDetailDto {
    private Long id;
    private String name;
    private String email;
    private String role;
}
