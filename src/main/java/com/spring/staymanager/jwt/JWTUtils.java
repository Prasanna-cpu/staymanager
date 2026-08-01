package com.spring.staymanager.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

@Service
public class JWTUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.header.name}")
    private String jwtHeader;

    @Value("${jwt.access.expiration}")
    private Long jwtAccessExpiration;

    @Value("${jwt.refresh.expiration}")
    private Long jwtRefreshExpiration;

    private SecretKey key;

    @PostConstruct
    private void init(){
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public static String populateAuthorities(Collection<? extends GrantedAuthority> authorities){
        Set<String> authoritiesSet = new HashSet<>();
        for(GrantedAuthority authority : authorities){
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(" ", authoritiesSet);
    }

    public String generateAccessTokenFromAuthentication(Authentication authentication){
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roles = populateAuthorities(authorities);


        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtAccessExpiration))
                .subject(authentication.getName())
                .claim("authorities", roles)
                .claim("type", "access")
                .claim("email", authentication.getName())
                .signWith(key)
                .compact();

    }

    public String generateRefreshTokenFromAuthentication(Authentication authentication){
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roles = populateAuthorities(authorities);

        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtRefreshExpiration))
                .subject(authentication.getName())
                .claim("type", "refresh")
                .claim("email", authentication.getName())
                .signWith(key)
                .compact();
    }

    public String getAccessTokenFromUserDetails(UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtAccessExpiration))
                .claim("type", "access")
                .claim("email", userDetails.getUsername())
                .signWith(key)
                .compact();
    }

    public String getRefreshTokenFromUserDetails(UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtRefreshExpiration))
                .claim("type", "refresh")
                .claim("email", userDetails.getUsername())
                .signWith(key)
                .compact();
    }


    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        return claimsTFunction.apply(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload());
    }

    public String extractUsername(String token) {
        return extractClaims(token, claims -> claims.get("email", String.class));
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }


}
