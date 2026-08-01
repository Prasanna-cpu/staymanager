package com.spring.staymanager.config;

import com.spring.staymanager.jwt.JWTUtils;
import com.spring.staymanager.service.implementation.CustomUserDetailsServiceImplementation;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {


    private final JWTUtils jWTUtils;
    private final CustomUserDetailsServiceImplementation customUserDetailsService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.header.name}")
    private String jwtHeaderName;



    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String jwt = request.getHeader(jwtHeaderName != null ? jwtHeaderName : "Authorization");

        if(jwtSecret == null){
            throw new BadCredentialsException("JWT secret is not configured");
        }

        if(jwt != null && jwt.startsWith("Bearer ")){
            jwt = jwt.substring(7);
            String userEmail = jWTUtils.extractUsername(jwt);
            try{
                if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
                    if(jWTUtils.isValidToken(jwt, userDetails)){
                        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
                        Claims claims = Jwts
                                .parser()
                                .verifyWith(key)
                                .build()
                                .parseSignedClaims(jwt)
                                .getPayload();

                        String type = (String) claims.get("type");

                        if(!"access".equals(type)){
                            filterChain.doFilter(request, response);
                            return;
                        }

                        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        securityContext.setAuthentication(token);
                        SecurityContextHolder.setContext(securityContext);
                    }
                }
            }
            catch(MalformedJwtException e){
                throw new BadCredentialsException("Invalid JWT token: " + e.getMessage());
            }
            catch(ExpiredJwtException e) {
                throw new BadCredentialsException("Expired JWT token: " + e.getMessage());
            }
            catch (Exception e){
                throw new BadCredentialsException("Exception : " + e.getMessage());
            }

        }
        filterChain.doFilter(request, response);

    }
}
