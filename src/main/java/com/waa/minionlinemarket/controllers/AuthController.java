package com.waa.minionlinemarket.controllers;

import com.waa.minionlinemarket.models.dtos.requests.LoginDto;
import com.waa.minionlinemarket.models.dtos.requests.UserDto;
import com.waa.minionlinemarket.services.spec.AuthService;
import com.waa.minionlinemarket.services.spec.MyUserService;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    AuthService authService;
    MyUserService userService;

    @Value("${security.access.jwt.expiration-time}")
    private long accessExpiration;

    public AuthController(
            AuthService authService,
            MyUserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        // set accessToken to cookie header
        String accessToken = authService.login(loginDto).get("accessToken");
        ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(false)
                .maxAge(accessExpiration)
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return authService.login(loginDto).get("msg");
    }

    @PostMapping("/register")
    public String postUser(@RequestBody UserDto userDto) {
        return authService.register(userDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
