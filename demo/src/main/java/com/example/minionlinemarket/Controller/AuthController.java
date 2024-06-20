package com.example.minionlinemarket.Controller;

import com.example.minionlinemarket.Services.AuthService;
import com.example.minionlinemarket.Services.MyUserService;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
import com.example.minionlinemarket.model.MyUser;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    AuthService authService;
    MyUserService userService;

    @Value("${security.access.jwt.expiration-time}")
    private long accessExpiration;

    public AuthController(
            AuthService authService,
            MyUserService userService
    ) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(@RequestBody MyUser user, HttpServletResponse response) {
        // set accessToken to cookie header
        String accessToken = authService.login(user).get("accessToken");
        String refreshToken = authService.login(user).get("refreshToken");
        ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(false)
                .maxAge(accessExpiration)
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return authService.login(user).get("msg");
    }

    @PostMapping("/register")
    public String postUser(@RequestBody MyUser user) {
        return authService.register(user);
    }

}
