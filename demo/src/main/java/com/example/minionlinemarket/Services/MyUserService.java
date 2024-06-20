package com.example.minionlinemarket.Services;

import org.springframework.security.core.userdetails.UserDetailsService;
import com.example.minionlinemarket.model.MyUser;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface MyUserService  extends UserDetailsService {
    MyUser save(MyUser user) ;
    MyUser findById(long id);
    void deleteById(long id);
    List<MyUser> findAll();
//    @Override
//    public default MyUser loadUserByUsername(String email) throws UsernameNotFoundException {
//        return userRepository
//                .findByEmail(email)
//                .orElseThrow(
//                        () -> new UsernameNotFoundException("User not Found")
//                );
//
//    }
}
