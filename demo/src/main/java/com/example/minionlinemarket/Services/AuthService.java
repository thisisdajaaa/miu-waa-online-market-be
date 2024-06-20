package com.example.minionlinemarket.Services;

import com.example.minionlinemarket.Model.Dto.Request.LoginDto;
import com.example.minionlinemarket.Model.Dto.Request.UserDto;
import com.example.minionlinemarket.Model.MyUser;

import java.util.Map;

public interface AuthService {
    Map<String,String> login(LoginDto loginDto);
    String register(UserDto userDto);
    String refreshToken(String refreshToken);
//    void logout(String refreshToken);
//    void verify(String token);
}
