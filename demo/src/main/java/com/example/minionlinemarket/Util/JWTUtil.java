package com.example.minionlinemarket.Util;

import com.example.minionlinemarket.Services.MyUserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtil {
    MyUserService myUserService;
    @Value("${security.jwt.secret-key}")
    private String secret;
    @Value("${security.access.jwt.expiration-time}")
    private long accessExpiration;
    @Value("${security.refresh.jwt.expiration-time}")
    private long refreshExpiration;

    public JWTUtil(MyUserService myUserService) {
        this.myUserService = myUserService;
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .parseClaimsJws(token)
                .getBody();
    }

    // Claim from JWT
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token) {
        final Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    public String generateToken(UserDetails userDetails, long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities());
        String subject = userDetails.getUsername();
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, accessExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, refreshExpiration);
    }

    public UserDetails getUserDetails(String token) {
        Claims claims = getAllClaimsFromToken(token);
        UserDetails userDetails = myUserService.loadUserByUsername(claims.getSubject());
        return userDetails;
    }

    public boolean validateToken(String token) {
        try {
            Jwts
                    .parser()
                    .setSigningKey(getSignInKey())
                    .parseClaimsJws(token);
            return true;
        } catch (
                MalformedJwtException |
                ExpiredJwtException |
                UsernameNotFoundException |
                IllegalArgumentException |
                UnsupportedJwtException e
        ) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getAllClaimsFromToken(token);
        UserDetails userDetails = myUserService.loadUserByUsername(claims.getSubject());
        // LEFT THIS IN BY PURPOSE
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        return authentication;
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
