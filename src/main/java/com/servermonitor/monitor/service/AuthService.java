package com.servermonitor.monitor.service;

import com.servermonitor.monitor.dto.RegisterRequest;
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
    private final JwtService jwtService;
    private final OperatorRepository operatorRepository;
    private final PasswordEncoder passwordEncoder;

    public String login(String username, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            return jwtService.generateToken(username);
        } catch (BadCredentialsException e) {
            throw e;
        }
    }

    public String register(RegisterRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Operator operator = Operator.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(encodedPassword)
                .build();
        operatorRepository.save(operator);

        return jwtService.generateToken(operator.getUsername());
    }
}
