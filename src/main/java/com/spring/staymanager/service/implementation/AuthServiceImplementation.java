package com.spring.staymanager.service.implementation;

import com.spring.staymanager.dto.UserDTO;
import com.spring.staymanager.entity.User;
import com.spring.staymanager.enums.Role;
import com.spring.staymanager.exception.BadRequestException;
import com.spring.staymanager.exception.ConflictingResourceException;
import com.spring.staymanager.exception.ObjectNotFoundException;
import com.spring.staymanager.exception.UnauthorizedAccessException;
import com.spring.staymanager.jwt.JWTUtils;
import com.spring.staymanager.mapper.UserMapper;
import com.spring.staymanager.repository.UserRepository;
import com.spring.staymanager.request.LoginRequest;
import com.spring.staymanager.request.RegisterRequest;
import com.spring.staymanager.response.LoginResponse;
import com.spring.staymanager.response.RegisterResponse;
import com.spring.staymanager.service.abstraction.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.webmvc.autoconfigure.error.AbstractErrorController;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, ObjectNotFoundException.class, ConflictingResourceException.class, UnauthorizedAccessException.class})
@Slf4j
public class AuthServiceImplementation implements AuthService {

    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AbstractErrorController abstractErrorController;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        RegisterResponse registerResponse = new RegisterResponse();
        User user = new User();

        String requestedRole = request.getRole() == null ? null : request.getRole().toString();

        if(requestedRole == null || requestedRole.isEmpty()) {
            requestedRole = "ROLE_USER";
        }
        else if(!requestedRole.equals("ROLE_USER") && !requestedRole.equals("ROLE_ADMIN")) {
            throw new BadRequestException("Invalid role");
        }

        Boolean isUserExists = userRepository.existsByEmail(request.getEmail());
        if(isUserExists) {
            throw new ConflictingResourceException("User already exists");
        }

        if(!Objects.equals(request.getPassword(), request.getConfirmPassword())){
            throw new BadRequestException("Password and Confirm Password do not match");
        }

        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(requestedRole));
        user.setName(request.getName());
        user.setPhoneNumber(request.getPhoneNumber());

        User savedUser = userRepository.save(user);
        UserDTO savedUserDTO = UserMapper.mapToUserDTO(savedUser);

        String accessToken = jwtUtils.getAccessTokenFromUserDetails(savedUser);
        String refreshToken = jwtUtils.getRefreshTokenFromUserDetails(savedUser);

        registerResponse.setData(savedUserDTO);
        registerResponse.setAccessToken(accessToken);
        registerResponse.setRefreshToken(refreshToken);

        return registerResponse;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        LoginResponse loginResponse = new LoginResponse();

        Boolean isUserExists = userRepository.existsByEmail(request.getEmail());
        if(!isUserExists) {
            throw new UnauthorizedAccessException("User not found, please register");
        }

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (AuthenticationException e) {
            throw new UnauthorizedAccessException("Invalid credentials");
        }

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new UnauthorizedAccessException("Invalid credentials")
        );
        String accessToken = jwtUtils.getAccessTokenFromUserDetails(user);
        String refreshToken = jwtUtils.getRefreshTokenFromUserDetails(user);

        loginResponse.setAccessToken(accessToken);
        loginResponse.setRefreshToken(refreshToken);

        return loginResponse;
    }
}
