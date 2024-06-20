package com.example.minionlinemarket.Services.Imp;

import com.example.minionlinemarket.Services.AuthService;
import com.example.minionlinemarket.Services.MyUserService;
import com.example.minionlinemarket.Model.MyUser;
import com.example.minionlinemarket.utils.JWTUtil;
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
    MyUserService userService;

    private final JWTUtil jwtUtil;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            JWTUtil jwtUtil,
            MyUserService userService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
    }


    @Override
    public Map<String, String> login(MyUser user) {

        Authentication result = null;
        try {
            result = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    user.getEmail(),
                                    user.getPassword()
                            )
                    );

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(e.getMessage());
        }

        UserDetails authenticatedUser = (UserDetails) result.getPrincipal();

        final String accessToken = jwtUtil.generateAccessToken(authenticatedUser);
        final String refreshToken = jwtUtil.generateRefreshToken(authenticatedUser);

        return Map.of(
                "msg", "Success! Please check your email to verify account",
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }

    @Override
    public String register(MyUser user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "Success! Your account has been registered";
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
