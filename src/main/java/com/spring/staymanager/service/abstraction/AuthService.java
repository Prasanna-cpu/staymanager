package com.spring.staymanager.service.abstraction;

import com.spring.staymanager.request.LoginRequest;
import com.spring.staymanager.request.RegisterRequest;
import com.spring.staymanager.response.LoginResponse;
import com.spring.staymanager.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
