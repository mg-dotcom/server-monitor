package com.servermonitor.monitor.service;

import com.servermonitor.monitor.dto.auth.RegisterRequest;
import com.servermonitor.monitor.model.Operator;
import com.servermonitor.monitor.repository.OperatorRepository;
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
    private final OperatorRepository operatorRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public String login(String username, String password) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            Operator operator = operatorRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Operator not found"));

            return jwtService.generateToken(username, operator.getRole().name());
        } catch (BadCredentialsException e) {
            throw e;
        }
    }
    public String register(RegisterRequest request) {
        if (operatorRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        Operator operator = Operator.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        operatorRepository.save(operator);

        return jwtService.generateToken(operator.getUsername(), operator.getRole().name());
    }
}
