package com.example.minionlinemarket.Controller;

import com.example.minionlinemarket.Services.MyUserService;
import com.example.minionlinemarket.Model.MyUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
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

    @PreAuthorize("hasAnyAuthority('ADMIN')")
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
    public MyUser showMe() {
        MyUser auth = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (auth == null) {
            throw new RuntimeException("User not authenticated");
        }
        return auth;
    }
}
