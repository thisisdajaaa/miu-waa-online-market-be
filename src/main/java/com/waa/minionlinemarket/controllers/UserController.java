package com.waa.minionlinemarket.controllers;

import com.waa.minionlinemarket.models.dtos.responses.AuthenticationDetailDto;
import com.waa.minionlinemarket.services.spec.MyUserService;
import com.waa.minionlinemarket.models.MyUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    MyUserService myUserService;
    BCryptPasswordEncoder bCryptPasswordEncoder;


    public UserController(
            MyUserService myUserService,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.myUserService = myUserService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PreAuthorize("hasAnyAuthority('BUYER')")
    @GetMapping
    public List<MyUser> getAll() {
        return myUserService.findAll();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        myUserService.deleteById(id);
    }

    @PreAuthorize("hasAnyAuthority('BUYER', 'SELLER', 'ADMIN')")
    @GetMapping("/showMe")
    public AuthenticationDetailDto showMe() {
        MyUser auth = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (auth == null) {
            throw new RuntimeException("User not authenticated");
        }
        return convertToDTO(auth);
    }

    private AuthenticationDetailDto convertToDTO(MyUser user) {
        return AuthenticationDetailDto.builder().id(user.getId()).name(user.getName()).role(user.getRole().name()).email(user.getEmail()).build();
    }
}
