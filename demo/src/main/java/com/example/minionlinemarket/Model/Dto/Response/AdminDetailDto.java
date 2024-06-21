package com.example.minionlinemarket.Model.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDetailDto {
    private Long id;
    private boolean isApproved;
    private String name;
    private String password;
    private String email;
}
