package com.example.minionlinemarket.Services;

import com.example.minionlinemarket.Model.Dto.Request.AdminDTO;
import com.example.minionlinemarket.Model.Dto.Request.SellerDto;
import com.example.minionlinemarket.Model.Dto.Response.AdminDetailDto;
import com.example.minionlinemarket.Model.Dto.Response.SellerDetailDto;

import java.util.List;

public interface AdminService {
    List<AdminDTO> findAll();
    AdminDetailDto findById(Long id);
    AdminDetailDto save(AdminDTO adminDTO);
    void delete(AdminDetailDto sellerDetailDto);
    SellerDetailDto update(Long id, AdminDTO adminDTO);

}
