package com.servermonitor.monitor.controller;

import com.servermonitor.monitor.ApiResponse.ApiResponse;
import com.servermonitor.monitor.dto.auth.LoginRequest;
import com.servermonitor.monitor.dto.auth.MeResponse;
import com.servermonitor.monitor.dto.auth.RegisterRequest;
import com.servermonitor.monitor.dto.auth.AuthResponse;
import com.servermonitor.monitor.model.Operator;
import com.servermonitor.monitor.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request){
        String token = authService.login(
                request.getUsername(),
                request.getPassword()
        );

        return ResponseEntity.ok(ApiResponse.ok(new AuthResponse(token)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody RegisterRequest request) {
        String token = authService.register(request);

        return ResponseEntity.ok(ApiResponse.ok(new AuthResponse(token)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MeResponse>> me() {
        Operator operator = (Operator) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(ApiResponse.ok(
                MeResponse.builder()
                        .name(operator.getFirstName() + " " + operator.getLastName())
                        .username(operator.getUsername())
                        .role(operator.getRole())
                        .build()
        ));
    }
}