package com.waa.minionlinemarket.services.impl;

import com.waa.minionlinemarket.models.Admin;
import com.waa.minionlinemarket.models.dtos.requests.BuyerDto;
import com.waa.minionlinemarket.repositories.AdminRepository;
import com.waa.minionlinemarket.repositories.UserRepository;
import com.waa.minionlinemarket.services.spec.BuyerService;
import com.waa.minionlinemarket.services.spec.MyUserService;
import com.waa.minionlinemarket.services.spec.SellerService;
import com.waa.minionlinemarket.models.dtos.requests.SellerDto;
import com.waa.minionlinemarket.models.MyUser;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements MyUserService {
    private UserRepository userRepository;
    private final SellerService sellerService;
    private final AdminRepository adminRepo;
    private final BuyerService buyerService;

    public UserServiceImpl(UserRepository userRepository, SellerService sellerService, AdminRepository adminRepo, BuyerService buyerService) {
        this.userRepository = userRepository;
        this.sellerService = sellerService;
        this.adminRepo = adminRepo;
        this.buyerService = buyerService;
    }

    @Override
    public MyUser save(MyUser user) {

            System.out.println("here is the role"+user.getRole());
            if(user.getRole().toString().equals("SELLER")){
                SellerDto newseller = new SellerDto();
                newseller.setName(user.getName());
                newseller.setEmail(user.getEmail());
                newseller.setPassword(user.getPassword());
                newseller.setRole(user.getRole());
                newseller.setApproved(false);
                sellerService.save(newseller);
            }
            if(user.getRole().toString().equals("ADMIN")){
                Admin admin = new Admin();
                admin.setName(user.getName());
                admin.setEmail(user.getEmail());
                admin.setRole(user.getRole());
                admin.setPassword(user.getPassword());
                adminRepo.save(admin);
            }

            if(user.getRole().toString().equals("BUYER")){
                BuyerDto buyer=new BuyerDto();
                buyer.setName(user.getName());
                buyer.setEmail(user.getEmail());
                buyer.setPassword(user.getPassword());
                buyer.setRole(user.getRole());
                buyerService.save(buyer);
            }



        return user;
    }


    @Override
    public MyUser findById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<MyUser> findAll() {
        return userRepository.findAll();
    }

    @Override
    public MyUser loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User not Found")
                );

    }
}
