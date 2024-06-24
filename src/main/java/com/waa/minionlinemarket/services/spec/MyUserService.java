package com.waa.minionlinemarket.services.spec;

import com.waa.minionlinemarket.models.MyUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface MyUserService  extends UserDetailsService {
    MyUser save(MyUser user) ;
    MyUser findById(long id);
    void deleteById(long id);
    List<MyUser> findAll();
}
