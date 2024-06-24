package com.waa.minionlinemarket.services.impl;

import com.waa.minionlinemarket.configurations.MapperConfiguration;
import com.waa.minionlinemarket.models.dtos.requests.LoginDto;
import com.waa.minionlinemarket.models.dtos.requests.UserDto;
import com.waa.minionlinemarket.services.spec.AuthService;
import com.waa.minionlinemarket.services.spec.MyUserService;
import com.waa.minionlinemarket.utils.JWTUtil;
import com.waa.minionlinemarket.models.MyUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final MapperConfiguration mapperConfiguration;
    MyUserService userService;

    private final JWTUtil jwtUtil;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            BCryptPasswordEncoder bCryptPasswordEncoder, MapperConfiguration mapperConfiguration,
            JWTUtil jwtUtil,
            MyUserService userService) {
        this.authenticationManager = authenticationManager;
        this.mapperConfiguration = mapperConfiguration;
        this.jwtUtil = jwtUtil;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
    }

    @Override
    public Map<String, String> login(LoginDto loginDto) {

        Authentication result = null;
        try {
            result = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginDto.getEmail(),
                                    loginDto.getPassword()));

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(e.getMessage());
        }

        UserDetails authenticatedUser = (UserDetails) result.getPrincipal();

        final String accessToken = jwtUtil.generateAccessToken(authenticatedUser);
        final String refreshToken = jwtUtil.generateRefreshToken(authenticatedUser);

        return Map.of(
                "msg", "Success! Please check your email to verify account",
                "accessToken", accessToken,
                "refreshToken", refreshToken);
    }

    @Override
    public String register(UserDto userDto) {
        try {
            userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            userService.save(mapperConfiguration.convert(userDto, MyUser.class));
            return "Success! Your account has been registered";
        } catch (Exception e) {
            e.printStackTrace();
            if(userDto.getPassword() == null){
                return "you have to enter a password";
            }
            if(userDto.getRole()== null){
                return "you have to select a role";
            }
            if(userDto.getEmail() == null){
                return "you have to enter an email";
            }
            String fullMessage = e.getMessage();
            System.out.println("Error: " + fullMessage);


            String specificPart = fullMessage.split(":")[2];
            String lastone=specificPart.split("]")[0];
            return "Error! " + lastone;
        }
    }

    @Override
    public String refreshToken(String refreshToken) {
        boolean isRefreshTokenValid = jwtUtil.validateToken(refreshToken);
        if (isRefreshTokenValid) {
            var isAccessTokenExpired = jwtUtil.isTokenExpired(refreshToken);
            if (isAccessTokenExpired) {
                System.out.println("ACCESS_TOKEN_EXPIRED");
            } else {
                System.out.println("ACCESS_TOKEN_VALID");
            }

            return jwtUtil.generateAccessToken(jwtUtil.getUserDetails(refreshToken));
        }
        return "INVALID_REFRESH_TOKEN";
    }

}
