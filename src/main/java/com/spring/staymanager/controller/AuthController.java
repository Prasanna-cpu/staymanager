package com.spring.staymanager.controller;


import com.spring.staymanager.request.LoginRequest;
import com.spring.staymanager.request.RegisterRequest;
import com.spring.staymanager.response.ApiResponse;
import com.spring.staymanager.response.LoginResponse;
import com.spring.staymanager.response.RegisterResponse;
import com.spring.staymanager.service.abstraction.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerHandler(
            @Valid @RequestBody RegisterRequest registerRequest
    ) {
        RegisterResponse registerResponse = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse(
                        registerResponse,
                        "User Registered Successfully",
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginHandler(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        loginResponse,
                        "User Logged In Successfully",
                        HttpStatus.OK.value(),
                        HttpStatus.OK
                )
        );
    }

}
