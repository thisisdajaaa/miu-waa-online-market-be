package com.example.minionlinemarket.Services;

import com.example.minionlinemarket.Model.MyUser;

import java.util.Map;

public interface AuthService {
    Map<String,String> login(MyUser user);
    String register(MyUser user);
    String refreshToken(String refreshToken);
//    void logout(String refreshToken);
//    void verify(String token);
}
