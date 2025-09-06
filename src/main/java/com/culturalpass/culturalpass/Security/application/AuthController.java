package com.culturalpass.culturalpass.Security.application;

import com.culturalpass.culturalpass.Security.domain.AuthService;
import com.culturalpass.culturalpass.Security.dto.LoginRequestDto;
import com.culturalpass.culturalpass.Security.dto.LoginResponseDto;
import com.culturalpass.culturalpass.Security.dto.RegisterRequestDto;
import com.culturalpass.culturalpass.Security.dto.RegisterResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        LoginResponseDto response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequest) {
        RegisterResponseDto response = authService.register(registerRequest);
        return ResponseEntity.ok(response);
    }
}