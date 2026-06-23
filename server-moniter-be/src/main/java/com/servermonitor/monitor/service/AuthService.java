package com.servermonitor.monitor.service;

import com.servermonitor.monitor.dto.auth.RegisterRequest;
import com.servermonitor.monitor.model.User;
import com.servermonitor.monitor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public String login(String username, String password) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            User operator = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

            return jwtService.generateToken(username, operator.getRole().name());
        } catch (BadCredentialsException e) {
            throw e;
        }
    }
    public String register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User operator = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(operator);

        return jwtService.generateToken(operator.getUsername(), operator.getRole().name());
    }
}
