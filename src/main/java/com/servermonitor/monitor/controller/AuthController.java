package com.servermonitor.monitor.controller;

import com.servermonitor.monitor.dto.ApiResponse;
import com.servermonitor.monitor.dto.LoginRequest;
import com.servermonitor.monitor.service.AuthService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest request){
        String token =  authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(ApiResponse.ok(token));
    }
}
