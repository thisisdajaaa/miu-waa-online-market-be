package com.waa.minionlinemarket.services.spec;

import com.waa.minionlinemarket.models.dtos.requests.LoginDto;
import com.waa.minionlinemarket.models.dtos.requests.UserDto;

import java.util.Map;

public interface AuthService {
    Map<String,String> login(LoginDto loginDto);
    String register(UserDto userDto);
    String refreshToken(String refreshToken);
}
