package com.waa.minionlinemarket.services.spec;

import com.waa.minionlinemarket.models.dtos.requests.AdminDTO;
import com.waa.minionlinemarket.models.dtos.responses.AdminDetailDto;
import com.waa.minionlinemarket.models.dtos.responses.SellerDetailDto;

import java.util.List;

public interface AdminService {
    List<AdminDTO> findAll();
    AdminDetailDto findById(Long id);
    AdminDetailDto save(AdminDTO adminDTO);
    void delete(AdminDetailDto sellerDetailDto);
    SellerDetailDto update(Long id, AdminDTO adminDTO);

}
